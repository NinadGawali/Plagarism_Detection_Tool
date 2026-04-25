/* ============================================================
   PLAGUEDETECT — Enhanced Frontend JS
   Features: Chart.js, Diff Viewer, PDF Export, History, Search, Sort
   ============================================================ */

"use strict";

// ─── DOM Refs ────────────────────────────────────────────────
const fileInput       = document.getElementById("file-input");
const dropzone        = document.getElementById("dropzone");
const fileListEl      = document.getElementById("file-list");
const fileCountEl     = document.getElementById("file-count");
const analyzeBtn      = document.getElementById("analyze-btn");
const clearBtn        = document.getElementById("clear-btn");
const statusEl        = document.getElementById("status");
const progressWrap    = document.getElementById("progress-wrap");
const progressFill    = document.getElementById("progress-fill");
const progressLabel   = document.getElementById("progress-label");
const resultsBody     = document.getElementById("results-body");
const detailView      = document.getElementById("detail-view");
const summaryGrid     = document.getElementById("summary-grid");
const thresholdSlider = document.getElementById("threshold-slider");
const thresholdVal    = document.getElementById("threshold-val");
const tableSearch     = document.getElementById("table-search");
const exportPdfBtn    = document.getElementById("export-pdf-btn");
const sortBtn         = document.getElementById("sort-btn");
const diffPairSelect  = document.getElementById("diff-pair-select");
const diffContainer   = document.getElementById("diff-container");
const historyList     = document.getElementById("history-list");
const clearHistoryBtn = document.getElementById("clear-history-btn");
const themeToggle     = document.getElementById("theme-toggle");
const gridCanvas      = document.getElementById("grid-canvas");

// ─── State ───────────────────────────────────────────────────
let selectedFiles   = [];
let latestResults   = [];
let filteredResults = [];
let scoreExplain    = {};
let chartInstance   = null;
let sortState       = { col: "strict", dir: "desc" };
let analysisHistory = JSON.parse(localStorage.getItem("pd_history") || "[]");

// ─── Animated Grid Background ────────────────────────────────
(function initGrid() {
  const ctx = gridCanvas.getContext("2d");
  let W, H;

  function resize() {
    W = gridCanvas.width  = window.innerWidth;
    H = gridCanvas.height = window.innerHeight;
  }

  resize();
  window.addEventListener("resize", resize);

  const COLS = 20, ROWS = 14;
  const dots = [];

  for (let r = 0; r < ROWS; r++) {
    for (let c = 0; c < COLS; c++) {
      dots.push({ r, c, opacity: Math.random() * 0.4, speed: 0.003 + Math.random() * 0.006, phase: Math.random() * Math.PI * 2 });
    }
  }

  let frame = 0;

  function draw() {
    ctx.clearRect(0, 0, W, H);
    const cellW = W / COLS, cellH = H / ROWS;
    const isDark = !document.body.classList.contains("light");

    dots.forEach(d => {
      d.phase += d.speed;
      const op = (Math.sin(d.phase) + 1) / 2 * 0.5;
      const x = d.c * cellW + cellW / 2;
      const y = d.r * cellH + cellH / 2;
      ctx.beginPath();
      ctx.arc(x, y, 1.2, 0, Math.PI * 2);
      ctx.fillStyle = isDark ? `rgba(0,212,170,${op})` : `rgba(0,150,120,${op * 0.5})`;
      ctx.fill();
    });

    // Draw grid lines
    ctx.strokeStyle = isDark ? "rgba(255,255,255,0.025)" : "rgba(0,0,0,0.04)";
    ctx.lineWidth = 1;
    for (let c = 0; c <= COLS; c++) {
      ctx.beginPath();
      ctx.moveTo(c * cellW, 0);
      ctx.lineTo(c * cellW, H);
      ctx.stroke();
    }
    for (let r = 0; r <= ROWS; r++) {
      ctx.beginPath();
      ctx.moveTo(0, r * cellH);
      ctx.lineTo(W, r * cellH);
      ctx.stroke();
    }

    requestAnimationFrame(draw);
  }

  draw();
})();

// ─── Theme Toggle ─────────────────────────────────────────────
themeToggle.addEventListener("click", () => {
  document.body.classList.toggle("light");
  localStorage.setItem("pd_theme", document.body.classList.contains("light") ? "light" : "dark");
});

if (localStorage.getItem("pd_theme") === "light") {
  document.body.classList.add("light");
}

// ─── Tab Navigation ──────────────────────────────────────────
document.querySelectorAll(".nav-link").forEach(link => {
  link.addEventListener("click", e => {
    e.preventDefault();
    const tab = link.dataset.tab;
    document.querySelectorAll(".nav-link").forEach(l => l.classList.remove("active"));
    document.querySelectorAll(".tab-content").forEach(t => t.classList.remove("active"));
    link.classList.add("active");
    document.getElementById(`tab-${tab}`).classList.add("active");

    if (tab === "history") renderHistory();
    if (tab === "diff") renderDiffSelector();
  });
});

// ─── Threshold Slider ────────────────────────────────────────
thresholdSlider.addEventListener("input", () => {
  thresholdVal.textContent = `${thresholdSlider.value}%`;
  if (latestResults.length) {
    applyThreshold();
    renderTable(filteredResults);
    renderSummary();
    renderChart();
    renderHeatmap();
  }
});

function getThreshold() {
  return parseInt(thresholdSlider.value) / 100;
}

// ─── File Handling ───────────────────────────────────────────
function handleFiles(list) {
  const incoming = Array.from(list);
  incoming.forEach(f => {
    if (!selectedFiles.find(s => s.name === f.name && s.size === f.size)) {
      selectedFiles.push(f);
    }
  });
  renderFileList();
}

function renderFileList() {
  fileListEl.innerHTML = "";
  fileCountEl.textContent = `${selectedFiles.length} file${selectedFiles.length !== 1 ? "s" : ""} selected`;

  if (!selectedFiles.length) {
    const li = document.createElement("li");
    li.className = "file-item";
    li.style.color = "var(--ink-3)";
    li.style.justifyContent = "center";
    li.textContent = "No files selected yet.";
    fileListEl.appendChild(li);
  } else {
    selectedFiles.forEach((file, idx) => {
      const ext = file.name.split(".").pop().toUpperCase().slice(0, 4);
      const li = document.createElement("li");
      li.className = "file-item";
      li.innerHTML = `
        <div class="file-item-name">
          <div class="file-icon">${ext}</div>
          <span class="file-name-text">${escHtml(file.name)}</span>
        </div>
        <span class="file-size">${formatSize(file.size)}</span>
        <button class="file-remove" data-idx="${idx}" title="Remove">✕</button>
      `;
      fileListEl.appendChild(li);
    });

    fileListEl.querySelectorAll(".file-remove").forEach(btn => {
      btn.addEventListener("click", e => {
        e.stopPropagation();
        selectedFiles.splice(parseInt(btn.dataset.idx), 1);
        renderFileList();
      });
    });
  }

  analyzeBtn.disabled = selectedFiles.length < 2;
}

function formatSize(bytes) {
  if (bytes < 1024) return `${bytes}B`;
  if (bytes < 1024 * 1024) return `${(bytes / 1024).toFixed(1)}KB`;
  return `${(bytes / 1024 / 1024).toFixed(1)}MB`;
}

clearBtn.addEventListener("click", () => {
  selectedFiles = [];
  renderFileList();
});

fileInput.addEventListener("change", e => handleFiles(e.target.files));

["dragenter", "dragover"].forEach(ev => {
  dropzone.addEventListener(ev, e => { e.preventDefault(); dropzone.classList.add("dragover"); });
});

["dragleave", "drop"].forEach(ev => {
  dropzone.addEventListener(ev, e => { e.preventDefault(); dropzone.classList.remove("dragover"); });
});

dropzone.addEventListener("drop", e => handleFiles(e.dataTransfer.files));

// ─── Progress Simulation ─────────────────────────────────────
function startProgress() {
  progressWrap.classList.remove("hidden");
  const steps = [
    [10, "Reading file contents..."],
    [25, "Tokenizing source code..."],
    [42, "Running N-Gram analysis..."],
    [58, "Applying Rabin-Karp hashing..."],
    [70, "Computing Edit Distance..."],
    [83, "Detecting similar blocks..."],
    [92, "Compiling results..."],
  ];
  let i = 0;

  function step() {
    if (i < steps.length) {
      const [pct, label] = steps[i++];
      progressFill.style.width = pct + "%";
      progressLabel.textContent = label;
      setTimeout(step, 280 + Math.random() * 180);
    }
  }

  step();
}

function finishProgress() {
  progressFill.style.width = "100%";
  progressLabel.textContent = "Analysis complete!";
  setTimeout(() => progressWrap.classList.add("hidden"), 1200);
}

// ─── Analysis ────────────────────────────────────────────────
analyzeBtn.addEventListener("click", analyze);

async function analyze() {
  const formData = new FormData();
  selectedFiles.forEach(f => formData.append("files", f));

  analyzeBtn.disabled = true;
  setStatus("Running plagiarism analysis...");
  startProgress();

  try {
    const resp = await fetch("/api/analyze", { method: "POST", body: formData });
    const data = await resp.json();

    if (!resp.ok) throw new Error(data.error || "Analysis failed.");

    scoreExplain  = data.scoreExplanations || {};
    latestResults = data.results || [];

    applyThreshold();
    updateSummary(data);
    renderTable(filteredResults);
    renderChart();
    renderHeatmap();

    // Switch to results tab
    switchTab("results");
    finishProgress();
    setStatus("Analysis complete. Click a row to inspect details.", "success");

    // Save to history
    saveHistory(data);
    toast("Analysis complete!", "success");

  } catch (err) {
    finishProgress();
    setStatus(err.message || "Unexpected error.", "error");
    toast(err.message, "error");
  } finally {
    analyzeBtn.disabled = selectedFiles.length < 2;
  }
}

function applyThreshold() {
  const t = getThreshold();
  filteredResults = latestResults.map(r => ({
    ...r,
    suspicious: (r.strictSimilarity || r.averageSimilarity) >= t,
  }));
}

// ─── Summary ─────────────────────────────────────────────────
function updateSummary(data) {
  renderSummary();
}

function renderSummary() {
  if (!filteredResults.length) return;
  const suspicious = filteredResults.filter(r => r.suspicious).length;
  const total = filteredResults.length;
  const maxSim = Math.max(...filteredResults.map(r => r.strictSimilarity || r.averageSimilarity));
  const avgSim = filteredResults.reduce((s, r) => s + (r.strictSimilarity || r.averageSimilarity), 0) / total;

  const stats = [
    { key: "Files",       val: latestResults[0] ? "—" : "0",      cls: "" },
    { key: "Pairs",       val: total,                               cls: "" },
    { key: "Suspicious",  val: suspicious,                          cls: suspicious > 0 ? "danger" : "ok" },
    { key: "Max Sim.",    val: pct(maxSim),                         cls: maxSim >= 0.75 ? "danger" : maxSim >= 0.5 ? "warning" : "" },
    { key: "Avg Sim.",    val: pct(avgSim),                         cls: "" },
  ];

  // Derive file count from names
  const names = new Set();
  filteredResults.forEach(r => {
    names.add(r.fileA.split(/[/\\]/).pop());
    names.add(r.fileB.split(/[/\\]/).pop());
  });
  stats[0].val = names.size;

  summaryGrid.innerHTML = "";
  stats.forEach((s, i) => {
    const card = document.createElement("div");
    card.className = `stat-card ${s.cls}`;
    card.style.animationDelay = `${i * 0.06}s`;
    card.innerHTML = `<p class="stat-key">${s.key}</p><p class="stat-value ${s.cls}">${s.val}</p>`;
    summaryGrid.appendChild(card);
  });
}

// ─── Table ───────────────────────────────────────────────────
function renderTable(results) {
  resultsBody.innerHTML = "";

  const sorted = sortResults(results);

  if (!sorted.length) {
    resultsBody.innerHTML = `<tr><td colspan="6" style="text-align:center;color:var(--ink-3);padding:32px">No results found.</td></tr>`;
    return;
  }

  sorted.forEach((r, idx) => {
    const nameA = r.fileA.split(/[/\\]/).pop();
    const nameB = r.fileB.split(/[/\\]/).pop();
    const sim   = r.strictSimilarity || r.averageSimilarity;
    const color = sim >= 0.75 ? "var(--danger)" : sim >= 0.5 ? "var(--warning)" : "var(--ok)";

    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>
        <span class="pair-label">${escHtml(nameA)}</span>
        <span class="pair-vs">vs</span>
        <span class="pair-label">${escHtml(nameB)}</span>
      </td>
      <td>
        <div class="sim-bar-wrap">
          <div class="sim-bar-bg">
            <div class="sim-bar-fill" style="width:${Math.round(sim*100)}%;background:${color}"></div>
          </div>
          <span class="sim-val" style="color:${color}">${pct(sim)}</span>
        </div>
      </td>
      <td><span class="pill ${r.suspicious ? "warn" : "ok"}">${r.suspicious ? "Suspicious" : "Normal"}</span></td>
      <td style="font-family:var(--font-mono);font-size:0.8rem">${pct(r.scores?.ngram || 0)}</td>
      <td style="font-family:var(--font-mono);font-size:0.8rem">${pct(r.scores?.editNormalized || 0)}</td>
      <td>
        <div class="action-btns">
          <button class="row-btn diff" data-idx="${idx}">Diff</button>
          <button class="row-btn" data-detail="${idx}">Details</button>
        </div>
      </td>
    `;

    tr.addEventListener("click", e => {
      if (e.target.closest(".row-btn")) return;
      selectRow(tr, r);
    });

    tr.querySelector("[data-detail]").addEventListener("click", e => {
      e.stopPropagation();
      selectRow(tr, r);
    });

    tr.querySelector(".row-btn.diff").addEventListener("click", e => {
      e.stopPropagation();
      openDiff(r);
    });

    resultsBody.appendChild(tr);

    if (idx === 0) selectRow(tr, r);
  });
}

function selectRow(tr, r) {
  document.querySelectorAll("#results-body tr").forEach(t => t.classList.remove("selected"));
  tr.classList.add("selected");
  renderDetail(r);
  detailView.classList.remove("hidden");
  detailView.scrollIntoView({ behavior: "smooth", block: "nearest" });
}

function sortResults(results) {
  return [...results].sort((a, b) => {
    let va, vb;
    switch (sortState.col) {
      case "strict": va = a.strictSimilarity || a.averageSimilarity; vb = b.strictSimilarity || b.averageSimilarity; break;
      case "ngram":  va = a.scores?.ngram || 0; vb = b.scores?.ngram || 0; break;
      case "edit":   va = a.scores?.editNormalized || 0; vb = b.scores?.editNormalized || 0; break;
      case "pair":   va = a.fileA; vb = b.fileA; break;
      default:       va = a.strictSimilarity || a.averageSimilarity; vb = b.strictSimilarity || b.averageSimilarity;
    }
    if (typeof va === "string") return sortState.dir === "asc" ? va.localeCompare(vb) : vb.localeCompare(va);
    return sortState.dir === "asc" ? va - vb : vb - va;
  });
}

// Table search
tableSearch.addEventListener("input", () => {
  const q = tableSearch.value.toLowerCase();
  const filtered = q
    ? filteredResults.filter(r =>
        r.fileA.toLowerCase().includes(q) ||
        r.fileB.toLowerCase().includes(q)
      )
    : filteredResults;
  renderTable(filtered);
});

// Column sort
document.querySelectorAll("th.sortable").forEach(th => {
  th.addEventListener("click", () => {
    const col = th.dataset.col;
    if (sortState.col === col) {
      sortState.dir = sortState.dir === "asc" ? "desc" : "asc";
    } else {
      sortState.col = col;
      sortState.dir = "desc";
    }
    document.querySelectorAll("th.sortable").forEach(t => t.classList.remove("sorted-asc", "sorted-desc"));
    th.classList.add(sortState.dir === "asc" ? "sorted-asc" : "sorted-desc");
    renderTable(filteredResults);
  });
});

sortBtn.addEventListener("click", () => {
  sortState.dir = sortState.dir === "asc" ? "desc" : "asc";
  renderTable(filteredResults);
});

// ─── Detail View ─────────────────────────────────────────────
function renderDetail(r) {
  const nameA = r.fileA.split(/[/\\]/).pop();
  const nameB = r.fileB.split(/[/\\]/).pop();
  const sim   = r.strictSimilarity || r.averageSimilarity;

  const simClass = sim >= 0.75 ? "high" : sim >= 0.5 ? "medium" : "low";

  const metrics = [
    ["Strict Similarity",    pct(r.strictSimilarity || r.averageSimilarity), simClass],
    ["Legacy Average",       pct(r.averageSimilarity),                        ""],
    ["N-Gram Jaccard",       pct(r.scores?.ngram || 0),                       ""],
    ["KMP Match",            pct(r.scores?.kmp || 0),                         ""],
    ["Rabin-Karp",           pct(r.scores?.rabin || 0),                       ""],
    ["Suffix Array",         pct(r.scores?.suffix || 0),                      ""],
    ["Edit Distance",        pct(r.scores?.editNormalized || 0),               ""],
    ["Lines Matched A",      `${r.matchedLinesA||0}/${r.totalLinesA||0}`,     ""],
    ["Lines Matched B",      `${r.matchedLinesB||0}/${r.totalLinesB||0}`,     ""],
    ["Avg Line Similarity",  pct(r.averageLineSimilarity||0),                 ""],
  ];

  const metricsHtml = metrics.map(([label, val, cls]) => `
    <div class="metric">
      <p class="metric-label">${label}</p>
      <p class="metric-value ${cls}">${val}</p>
    </div>
  `).join("");

  const blocksHtml = (r.similarBlocks || []).map(block => `
    <div class="block-card">
      <p class="block-title">Lines ${block.startLineA}–${block.endLineA} ↔ ${block.startLineB}–${block.endLineB}</p>
      <p class="block-meta">${block.lineCount} lines · avg ${pct(block.averageSimilarity)}</p>
      <div class="line-snippet-list">
        ${(block.matches || []).slice(0, 4).map(m => `
          <div class="line-snippet">
            <p class="line-pair-head">A:${m.lineNumberA} ↔ B:${m.lineNumberB} · ${pct(m.similarity)}</p>
            <code>${escHtml(m.contentA)}</code>
            <code>${escHtml(m.contentB)}</code>
          </div>
        `).join("")}
      </div>
    </div>
  `).join("") || `<p class="muted">No consecutive similar blocks detected.</p>`;

  const lineMatchesHtml = (r.lineMatches || []).slice(0, 20).map(m => `
    <tr>
      <td>A:${m.lineNumberA}</td>
      <td>B:${m.lineNumberB}</td>
      <td style="font-family:var(--font-mono);font-size:0.78rem;color:${m.similarity>=0.75?"var(--danger)":"var(--ink)"}">${pct(m.similarity)}</td>
      <td><code>${escHtml(m.contentA)}</code></td>
      <td><code>${escHtml(m.contentB)}</code></td>
    </tr>
  `).join("") || `<tr><td colspan="5" style="color:var(--ink-3)">No high-confidence line matches found.</td></tr>`;

  const scoreHelpHtml = Object.entries(scoreExplain).map(([k, v]) => `
    <div class="score-help"><p>${k}</p><span>${v}</span></div>
  `).join("");

  detailView.innerHTML = `
    <div class="detail-head">
      <span>${escHtml(nameA)}</span>
      <span class="pair-sep">⟷</span>
      <span>${escHtml(nameB)}</span>
      <span class="pill ${r.suspicious ? "warn" : "ok"}" style="margin-left:auto">${r.suspicious ? "🚨 Suspicious" : "✓ Normal"}</span>
    </div>
    <div class="detail-grid">${metricsHtml}</div>
    ${scoreHelpHtml ? `<p class="section-title">Algorithm Explanations</p><div class="score-help-grid">${scoreHelpHtml}</div>` : ""}
    <p class="section-title">Similar Code Blocks</p>
    <div class="block-grid">${blocksHtml}</div>
    <p class="section-title">Top Line Matches</p>
    <div class="mini-table-wrap">
      <table class="mini-table">
        <thead>
          <tr><th>Line A</th><th>Line B</th><th>Similarity</th><th>Code A</th><th>Code B</th></tr>
        </thead>
        <tbody>${lineMatchesHtml}</tbody>
      </table>
    </div>
  `;
}

// ─── Chart ───────────────────────────────────────────────────
function renderChart() {
  const canvas = document.getElementById("similarity-chart");
  if (!canvas) return;
  if (chartInstance) { chartInstance.destroy(); chartInstance = null; }

  const labels = filteredResults.map((r, i) => {
    const a = r.fileA.split(/[/\\]/).pop().replace(/\.[^.]+$/, "");
    const b = r.fileB.split(/[/\\]/).pop().replace(/\.[^.]+$/, "");
    return `${a} vs ${b}`;
  });

  const vals   = filteredResults.map(r => Math.round((r.strictSimilarity || r.averageSimilarity) * 100));
  const colors = filteredResults.map(r => {
    const s = r.strictSimilarity || r.averageSimilarity;
    return s >= 0.75 ? "rgba(255,71,87,0.85)" : s >= 0.5 ? "rgba(255,165,2,0.85)" : "rgba(0,212,170,0.85)";
  });

  const isDark = !document.body.classList.contains("light");
  const textColor = isDark ? "#8b97a8" : "#4a5568";
  const gridColor = isDark ? "rgba(255,255,255,0.05)" : "rgba(0,0,0,0.06)";

  chartInstance = new Chart(canvas, {
    type: "bar",
    data: {
      labels,
      datasets: [{
        label: "Similarity %",
        data: vals,
        backgroundColor: colors,
        borderRadius: 6,
        borderSkipped: false,
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: { display: false },
        tooltip: {
          callbacks: {
            label: ctx => ` ${ctx.parsed.y}% similarity`,
            afterLabel: ctx => filteredResults[ctx.dataIndex]?.suspicious ? " 🚨 Suspicious" : " ✓ Normal"
          },
          backgroundColor: isDark ? "#1e242e" : "#fff",
          titleColor: isDark ? "#e8edf5" : "#0f1923",
          bodyColor: isDark ? "#8b97a8" : "#4a5568",
          borderColor: isDark ? "rgba(255,255,255,0.1)" : "rgba(0,0,0,0.1)",
          borderWidth: 1,
        }
      },
      scales: {
        x: {
          ticks: {
            color: textColor,
            font: { family: "'Syne', sans-serif", size: 10 },
            maxRotation: 30,
            callback: (val, i) => {
              const label = labels[i];
              return label.length > 14 ? label.slice(0, 14) + "…" : label;
            }
          },
          grid: { color: gridColor },
        },
        y: {
          min: 0,
          max: 100,
          ticks: {
            color: textColor,
            font: { family: "'JetBrains Mono', monospace", size: 10 },
            callback: v => v + "%"
          },
          grid: { color: gridColor },
        }
      }
    }
  });
}

// ─── Heatmap ─────────────────────────────────────────────────
function renderHeatmap() {
  const container = document.getElementById("heatmap-container");
  if (!container) return;

  // Collect all unique filenames
  const files = [];
  filteredResults.forEach(r => {
    const a = r.fileA.split(/[/\\]/).pop();
    const b = r.fileB.split(/[/\\]/).pop();
    if (!files.includes(a)) files.push(a);
    if (!files.includes(b)) files.push(b);
  });

  const n = files.length;
  if (n < 2) { container.innerHTML = `<p class="muted">Need ≥2 files for heatmap.</p>`; return; }

  // Build matrix
  const matrix = Array.from({ length: n }, () => Array(n).fill(null));
  filteredResults.forEach(r => {
    const a = files.indexOf(r.fileA.split(/[/\\]/).pop());
    const b = files.indexOf(r.fileB.split(/[/\\]/).pop());
    const sim = r.strictSimilarity || r.averageSimilarity;
    matrix[a][b] = sim;
    matrix[b][a] = sim;
  });

  const cellSize = Math.min(40, Math.floor(220 / n));
  container.style.gridTemplateColumns = `auto repeat(${n}, ${cellSize}px)`;
  container.innerHTML = "";

  // Header row
  const headerRow = document.createElement("div");
  headerRow.className = "heatmap-row";
  const emptyCell = document.createElement("div");
  emptyCell.style.cssText = `width:auto;height:${cellSize}px;`;
  headerRow.appendChild(emptyCell);

  files.forEach(f => {
    const cell = document.createElement("div");
    cell.style.cssText = `width:${cellSize}px;height:${cellSize}px;display:flex;align-items:flex-end;justify-content:center;padding:2px;`;
    const label = document.createElement("span");
    label.style.cssText = `font-size:0.6rem;color:var(--ink-2);writing-mode:vertical-rl;transform:rotate(180deg);white-space:nowrap;overflow:hidden;max-height:${cellSize}px;`;
    label.textContent = f.length > 8 ? f.slice(0, 7) + "…" : f;
    cell.appendChild(label);
    headerRow.appendChild(cell);
  });
  container.appendChild(headerRow);

  files.forEach((rowFile, ri) => {
    const row = document.createElement("div");
    row.className = "heatmap-row";

    const rowLabel = document.createElement("div");
    rowLabel.style.cssText = `display:flex;align-items:center;padding-right:6px;font-size:0.6rem;color:var(--ink-2);white-space:nowrap;overflow:hidden;max-width:60px;`;
    rowLabel.textContent = rowFile.length > 8 ? rowFile.slice(0, 7) + "…" : rowFile;
    row.appendChild(rowLabel);

    files.forEach((colFile, ci) => {
      const cell = document.createElement("div");
      cell.className = "heatmap-cell";
      cell.style.cssText = `width:${cellSize}px;height:${cellSize}px;border-radius:4px;`;

      if (ri === ci) {
        cell.style.background = "var(--surface-3)";
        cell.textContent = "—";
        cell.style.cssText += "font-size:0.65rem;color:var(--ink-3);display:flex;align-items:center;justify-content:center;";
      } else if (matrix[ri][ci] !== null) {
        const v = matrix[ri][ci];
        const r255 = v >= 0.75 ? 255 : Math.round(v / 0.75 * 100);
        const g255 = v >= 0.75 ? Math.round((1 - (v - 0.75) / 0.25) * 150) : 150;
        cell.style.background = `rgba(${r255},${g255},80,${0.3 + v * 0.7})`;
        cell.style.cursor = "pointer";
        cell.title = `${rowFile} vs ${colFile}: ${Math.round(v*100)}%`;
        cell.innerHTML = `<span style="font-size:0.55rem;font-weight:700;color:rgba(255,255,255,0.9)">${Math.round(v*100)}%</span>`;
      } else {
        cell.style.background = "var(--surface-2)";
      }

      row.appendChild(cell);
    });

    container.appendChild(row);
  });
}

// ─── Diff Viewer ─────────────────────────────────────────────
function renderDiffSelector() {
  diffPairSelect.innerHTML = `<option value="">— Select a pair to diff —</option>`;
  filteredResults.forEach((r, i) => {
    const a = r.fileA.split(/[/\\]/).pop();
    const b = r.fileB.split(/[/\\]/).pop();
    const opt = document.createElement("option");
    opt.value = i;
    opt.textContent = `${a} vs ${b} (${pct(r.strictSimilarity || r.averageSimilarity)})`;
    diffPairSelect.appendChild(opt);
  });
}

function openDiff(r) {
  switchTab("diff");
  renderDiffSelector();

  const idx = filteredResults.findIndex(x => x.fileA === r.fileA && x.fileB === r.fileB);
  if (idx >= 0) {
    diffPairSelect.value = idx;
    renderDiff(r);
  }
}

diffPairSelect.addEventListener("change", () => {
  const idx = parseInt(diffPairSelect.value);
  if (!isNaN(idx) && filteredResults[idx]) {
    renderDiff(filteredResults[idx]);
  }
});

function renderDiff(r) {
  const nameA = r.fileA.split(/[/\\]/).pop();
  const nameB = r.fileB.split(/[/\\]/).pop();
  const matches = r.lineMatches || [];

  // Build lookup: lineA -> match info
  const matchMapA = new Map();
  const matchMapB = new Map();
  matches.forEach(m => {
    matchMapA.set(m.lineNumberA, m);
    matchMapB.set(m.lineNumberB, m);
  });

  const linesA = buildDiffLines(r, "A", matchMapA);
  const linesB = buildDiffLines(r, "B", matchMapB);

  diffContainer.innerHTML = `
    <div class="diff-split">
      <div class="diff-pane">
        <div class="diff-pane-header">📄 ${escHtml(nameA)} (File A)</div>
        <div class="diff-lines" id="diff-pane-a">${linesA}</div>
      </div>
      <div class="diff-pane">
        <div class="diff-pane-header">📄 ${escHtml(nameB)} (File B)</div>
        <div class="diff-lines" id="diff-pane-b">${linesB}</div>
      </div>
    </div>
  `;
}

function buildDiffLines(r, side, matchMap) {
  // Pull lines from similar blocks and line matches
  const allLines = new Map();

  // From blocks
  (r.similarBlocks || []).forEach(block => {
    const startLine = side === "A" ? block.startLineA : block.startLineB;
    const endLine   = side === "A" ? block.endLineA   : block.endLineB;
    (block.matches || []).forEach(m => {
      const lineNum = side === "A" ? m.lineNumberA : m.lineNumberB;
      const code    = side === "A" ? m.contentA    : m.contentB;
      allLines.set(lineNum, { code, sim: m.similarity, type: "matched" });
    });
  });

  // From top line matches
  (r.lineMatches || []).forEach(m => {
    const lineNum = side === "A" ? m.lineNumberA : m.lineNumberB;
    const code    = side === "A" ? m.contentA    : m.contentB;
    if (!allLines.has(lineNum)) {
      allLines.set(lineNum, { code, sim: m.similarity, type: m.similarity >= 0.75 ? "matched" : "partial" });
    }
  });

  if (!allLines.size) {
    return `<div style="padding:24px;color:var(--ink-3);font-size:0.8rem;font-family:var(--font-mono)">No line data available for this file.</div>`;
  }

  const sortedNums = [...allLines.keys()].sort((a, b) => a - b);
  let html = "";
  let prevNum = -2;

  sortedNums.forEach(num => {
    if (num > prevNum + 1 && prevNum !== -2) {
      html += `<div class="diff-line"><div class="diff-line-num" style="border:none">···</div><div class="diff-line-code" style="color:var(--ink-3);font-style:italic">⋮ ${num - prevNum - 1} lines omitted ⋮</div></div>`;
    }

    const { code, sim, type } = allLines.get(num);
    const cls = sim >= 0.85 ? "matched" : sim >= 0.5 ? "partial" : "unique";
    html += `
      <div class="diff-line ${cls}">
        <div class="diff-line-num">${num}</div>
        <div class="diff-line-code">${escHtml(code || "")}</div>
      </div>
    `;
    prevNum = num;
  });

  return html;
}

// ─── PDF Export ──────────────────────────────────────────────
exportPdfBtn.addEventListener("click", exportPDF);

function exportPDF() {
  if (!filteredResults.length) { toast("Run an analysis first.", "error"); return; }

  const suspicious = filteredResults.filter(r => r.suspicious);
  const allFiles   = new Set();
  filteredResults.forEach(r => {
    allFiles.add(r.fileA.split(/[/\\]/).pop());
    allFiles.add(r.fileB.split(/[/\\]/).pop());
  });

  const rows = filteredResults.map(r => {
    const a   = r.fileA.split(/[/\\]/).pop();
    const b   = r.fileB.split(/[/\\]/).pop();
    const sim = pct(r.strictSimilarity || r.averageSimilarity);
    const flag = r.suspicious ? "🚨 SUSPICIOUS" : "✓ Normal";
    return `<tr style="background:${r.suspicious?"#fff0f0":"#f8fff8"}">
      <td style="padding:8px;border:1px solid #ddd">${escHtml(a)}</td>
      <td style="padding:8px;border:1px solid #ddd">${escHtml(b)}</td>
      <td style="padding:8px;border:1px solid #ddd;font-weight:bold;color:${r.suspicious?"#cc0000":"#006600"}">${sim}</td>
      <td style="padding:8px;border:1px solid #ddd">${flag}</td>
      <td style="padding:8px;border:1px solid #ddd">${pct(r.scores?.ngram||0)}</td>
      <td style="padding:8px;border:1px solid #ddd">${pct(r.scores?.editNormalized||0)}</td>
    </tr>`;
  }).join("");

  const html = `<!DOCTYPE html><html><head>
    <meta charset="UTF-8">
    <title>Plagiarism Detection Report</title>
    <style>
      body { font-family: 'Arial', sans-serif; margin: 0; padding: 40px; color: #111; }
      .header { border-bottom: 3px solid #111; padding-bottom: 20px; margin-bottom: 30px; }
      .logo { font-size: 24px; font-weight: 900; letter-spacing: -1px; }
      .logo span { color: #007755; }
      h2 { font-size: 13px; text-transform: uppercase; letter-spacing: 2px; color: #555; margin: 20px 0 10px; }
      .stat-row { display: flex; gap: 24px; margin-bottom: 30px; }
      .stat { border: 1px solid #ddd; border-radius: 8px; padding: 14px 20px; min-width: 120px; }
      .stat-key { font-size: 10px; text-transform: uppercase; letter-spacing: 1px; color: #777; margin-bottom: 6px; }
      .stat-val { font-size: 24px; font-weight: 900; }
      .stat-val.danger { color: #cc0000; }
      table { width: 100%; border-collapse: collapse; font-size: 12px; }
      th { background: #111; color: #fff; padding: 10px 8px; text-align: left; font-size: 10px; text-transform: uppercase; letter-spacing: 1px; }
      .footer { margin-top: 40px; font-size: 11px; color: #999; border-top: 1px solid #ddd; padding-top: 14px; }
      @media print { body { padding: 20px; } }
    </style>
  </head><body>
    <div class="header">
      <div class="logo">Plague<span>Detect</span></div>
      <p style="color:#555;font-size:13px;margin-top:6px">Code Plagiarism Analysis Report — Generated ${new Date().toLocaleString()}</p>
    </div>

    <div class="stat-row">
      <div class="stat"><div class="stat-key">Files Analyzed</div><div class="stat-val">${allFiles.size}</div></div>
      <div class="stat"><div class="stat-key">Pairs Compared</div><div class="stat-val">${filteredResults.length}</div></div>
      <div class="stat"><div class="stat-key">Suspicious Pairs</div><div class="stat-val danger">${suspicious.length}</div></div>
      <div class="stat"><div class="stat-key">Threshold Used</div><div class="stat-val">${thresholdSlider.value}%</div></div>
    </div>

    <h2>Comparison Results</h2>
    <table>
      <thead><tr><th>File A</th><th>File B</th><th>Similarity</th><th>Status</th><th>N-Gram</th><th>Edit Dist.</th></tr></thead>
      <tbody>${rows}</tbody>
    </table>

    ${suspicious.length ? `
    <h2 style="color:#cc0000">⚠ Suspicious Pairs Requiring Review</h2>
    <ul style="font-size:13px;line-height:2">
      ${suspicious.map(r => `<li><strong>${r.fileA.split(/[/\\]/).pop()}</strong> vs <strong>${r.fileB.split(/[/\\]/).pop()}</strong> — ${pct(r.strictSimilarity||r.averageSimilarity)}</li>`).join("")}
    </ul>` : `<p style="color:#006600;margin-top:20px">✓ No suspicious pairs detected above the ${thresholdSlider.value}% threshold.</p>`}

    <div class="footer">
      <strong>PlagueDetect</strong> — Academic Integrity System |
      Report generated: ${new Date().toISOString()} |
      Threshold: ${thresholdSlider.value}%
    </div>
  </body></html>`;

  const blob = new Blob([html], { type: "text/html" });
  const url  = URL.createObjectURL(blob);
  const a    = document.createElement("a");
  a.href     = url;
  a.download = `plagiarism-report-${Date.now()}.html`;
  a.click();
  URL.revokeObjectURL(url);
  toast("Report exported! Open in browser and print as PDF.", "success");
}

// ─── History ─────────────────────────────────────────────────
function saveHistory(data) {
  const entry = {
    id:        Date.now(),
    date:      new Date().toLocaleString(),
    files:     new Set(),
    pairs:     (data.results || []).length,
    suspicious: 0,
    maxSim:    0,
    results:   data.results || [],
  };

  (data.results || []).forEach(r => {
    entry.files.add(r.fileA.split(/[/\\]/).pop());
    entry.files.add(r.fileB.split(/[/\\]/).pop());
    if (r.suspicious) entry.suspicious++;
    const sim = r.strictSimilarity || r.averageSimilarity;
    if (sim > entry.maxSim) entry.maxSim = sim;
  });

  entry.files = entry.files.size;

  analysisHistory.unshift(entry);
  if (analysisHistory.length > 20) analysisHistory.pop();
  localStorage.setItem("pd_history", JSON.stringify(analysisHistory));
}

function renderHistory() {
  if (!analysisHistory.length) {
    historyList.innerHTML = `<div class="empty-state">
      <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><circle cx="12" cy="12" r="10"/><polyline points="12 6 12 12 16 14"/></svg>
      <p>No analyses saved yet. Run an analysis to build history.</p>
    </div>`;
    return;
  }

  historyList.innerHTML = "";
  analysisHistory.forEach((entry, i) => {
    const item = document.createElement("div");
    item.className = "history-item";
    item.style.animationDelay = `${i * 0.05}s`;
    item.innerHTML = `
      <div class="history-meta">
        <div class="history-title">${entry.files} files · ${entry.pairs} pairs</div>
        <div class="history-sub">${entry.date}</div>
      </div>
      <div class="history-stats">
        <div class="history-stat">
          <div class="history-stat-val ${entry.suspicious > 0 ? "danger" : "ok"}">${entry.suspicious}</div>
          <div class="history-stat-label">Suspicious</div>
        </div>
        <div class="history-stat">
          <div class="history-stat-val">${Math.round((entry.maxSim||0)*100)}%</div>
          <div class="history-stat-label">Max Sim.</div>
        </div>
      </div>
      <div class="history-actions">
        <button class="icon-btn" data-load="${i}">Load</button>
        <button class="icon-btn danger" data-delete="${i}">✕</button>
      </div>
    `;

    item.querySelector("[data-load]").addEventListener("click", () => {
      latestResults = entry.results || [];
      applyThreshold();
      renderTable(filteredResults);
      renderChart();
      renderHeatmap();
      renderSummary();
      switchTab("results");
      toast("Analysis loaded from history.", "info");
    });

    item.querySelector("[data-delete]").addEventListener("click", () => {
      analysisHistory.splice(i, 1);
      localStorage.setItem("pd_history", JSON.stringify(analysisHistory));
      renderHistory();
    });

    historyList.appendChild(item);
  });
}

clearHistoryBtn.addEventListener("click", () => {
  if (!confirm("Clear all analysis history?")) return;
  analysisHistory = [];
  localStorage.removeItem("pd_history");
  renderHistory();
  toast("History cleared.", "info");
});

// ─── Tab Helper ──────────────────────────────────────────────
function switchTab(tab) {
  document.querySelectorAll(".nav-link").forEach(l => l.classList.remove("active"));
  document.querySelectorAll(".tab-content").forEach(t => t.classList.remove("active"));
  document.querySelector(`[data-tab="${tab}"]`).classList.add("active");
  document.getElementById(`tab-${tab}`).classList.add("active");
}

// ─── Toast ───────────────────────────────────────────────────
let toastContainer = document.createElement("div");
toastContainer.className = "toast-container";
document.body.appendChild(toastContainer);

function toast(msg, type = "info") {
  const t = document.createElement("div");
  t.className = `toast ${type}`;
  t.textContent = msg;
  toastContainer.appendChild(t);
  setTimeout(() => { t.style.opacity = "0"; t.style.transition = "opacity 0.4s"; setTimeout(() => t.remove(), 400); }, 3500);
}

// ─── Utilities ───────────────────────────────────────────────
function pct(v) { return `${((v || 0) * 100).toFixed(1)}%`; }

function escHtml(str) {
  if (str === null || str === undefined) return "";
  return String(str)
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;")
    .replace(/"/g, "&quot;")
    .replace(/'/g, "&#039;");
}

function setStatus(msg, type = "") {
  statusEl.textContent = msg;
  statusEl.className = `status ${type}`;
}

// ─── Init ────────────────────────────────────────────────────
renderFileList();
renderHistory();