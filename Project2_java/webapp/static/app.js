const fileInput = document.getElementById("file-input");
const dropzone = document.getElementById("dropzone");
const fileList = document.getElementById("file-list");
const analyzeBtn = document.getElementById("analyze-btn");
const statusEl = document.getElementById("status");
const resultsSection = document.getElementById("results-section");
const summaryGrid = document.getElementById("summary-grid");
const resultsBody = document.getElementById("results-body");
const detailView = document.getElementById("detail-view");

let selectedFiles = [];
let latestResults = [];
let scoreExplanations = {};

function renderFileList() {
  fileList.innerHTML = "";

  if (!selectedFiles.length) {
    const empty = document.createElement("li");
    empty.className = "file-item";
    empty.textContent = "No files selected yet.";
    fileList.appendChild(empty);
  } else {
    selectedFiles.forEach((file) => {
      const item = document.createElement("li");
      item.className = "file-item";
      item.textContent = `${file.name} (${Math.ceil(file.size / 1024)} KB)`;
      fileList.appendChild(item);
    });
  }

  analyzeBtn.disabled = selectedFiles.length < 2;
}

function setStatus(message, isError = false) {
  statusEl.textContent = message;
  statusEl.classList.toggle("error", isError);
}

function updateSummary(data) {
  const summary = [
    ["Files Compared", data.totalFiles],
    ["Total Pairs", data.totalComparisons],
    ["Suspicious Pairs", data.suspiciousCount],
    ["Strict Threshold", `${Math.round(data.threshold * 100)}%`],
    ["Line Match Threshold", `${Math.round((data.lineSimilarityThreshold || 0) * 100)}%`],
  ];

  summaryGrid.innerHTML = "";
  summary.forEach(([key, value]) => {
    const card = document.createElement("div");
    card.className = "summary-card";
    card.innerHTML = `<p class="summary-key">${key}</p><p class="summary-value">${value}</p>`;
    summaryGrid.appendChild(card);
  });
}

function renderDetail(result) {
  const blocksHtml = (result.similarBlocks || [])
    .map(
      (block) => `
      <div class="block-card">
        <p class="block-title">Lines ${block.startLineA}-${block.endLineA} ↔ ${block.startLineB}-${block.endLineB}</p>
        <p class="block-meta">${block.lineCount} consecutive lines • ${formatPercent(block.averageSimilarity)}</p>
        <div class="line-snippet-list">
          ${(block.matches || [])
            .map(
              (m) => `
            <div class="line-snippet">
              <p class="line-pair-head">A:${m.lineNumberA} ↔ B:${m.lineNumberB} • ${formatPercent(m.similarity)}</p>
              <code>${escapeHtml(m.contentA)}</code>
              <code>${escapeHtml(m.contentB)}</code>
            </div>
          `
            )
            .join("")}
        </div>
      </div>
    `
    )
    .join("");

  const lineMatchesHtml = (result.lineMatches || [])
    .slice(0, 16)
    .map(
      (m) => `
      <tr>
        <td>A:${m.lineNumberA}</td>
        <td>B:${m.lineNumberB}</td>
        <td>${formatPercent(m.similarity)}</td>
        <td><code>${escapeHtml(m.contentA)}</code></td>
        <td><code>${escapeHtml(m.contentB)}</code></td>
      </tr>
    `
    )
    .join("");

  const scoreHelp = Object.entries(scoreExplanations)
    .map(([key, value]) => `<div class="score-help"><p>${key}</p><span>${value}</span></div>`)
    .join("");

  detailView.innerHTML = `
    <h3 class="detail-head">${result.fileA.split(/[\\/]/).pop()} vs ${result.fileB.split(/[\\/]/).pop()}</h3>
    <div class="detail-grid">
      ${metric("Strict Similarity", formatPercent(result.strictSimilarity || result.averageSimilarity))}
      ${metric("Legacy Average", formatPercent(result.averageSimilarity))}
      ${metric("N-Gram Jaccard", formatPercent(result.scores.ngram))}
      ${metric("KMP Match", formatPercent(result.scores.kmp))}
      ${metric("Rabin-Karp Match", formatPercent(result.scores.rabin))}
      ${metric("Suffix Match", formatPercent(result.scores.suffix))}
      ${metric("Edit Distance (Norm)", formatPercent(result.scores.editNormalized))}
      ${metric("Matched Lines A", `${result.matchedLinesA || 0}/${result.totalLinesA || 0}`)}
      ${metric("Matched Lines B", `${result.matchedLinesB || 0}/${result.totalLinesB || 0}`)}
      ${metric("Avg Line Similarity", formatPercent(result.averageLineSimilarity || 0))}
    </div>
    <h4 class="sub-head">Score Meaning</h4>
    <div class="score-help-grid">${scoreHelp}</div>
    <h4 class="sub-head">Similar Code Blocks</h4>
    <div class="block-grid">${blocksHtml || `<p class="muted">No long consecutive similar blocks found.</p>`}</div>
    <h4 class="sub-head">Top Similar Line Pairs</h4>
    <div class="mini-table-wrap">
      <table class="mini-table">
        <thead>
          <tr>
            <th>Line A</th>
            <th>Line B</th>
            <th>Similarity</th>
            <th>Code A</th>
            <th>Code B</th>
          </tr>
        </thead>
        <tbody>
          ${lineMatchesHtml || `<tr><td colspan="5">No high-confidence line matches found.</td></tr>`}
        </tbody>
      </table>
    </div>
  `;
}

function metric(label, value) {
  return `<div class="metric"><p class="metric-label">${label}</p><p class="metric-value">${value}</p></div>`;
}

function formatPercent(value) {
  return `${(value * 100).toFixed(1)}%`;
}

function renderTable(results) {
  resultsBody.innerHTML = "";

  results.forEach((result, index) => {
    const row = document.createElement("tr");
    const fileA = result.fileA.split(/[\\/]/).pop();
    const fileB = result.fileB.split(/[\\/]/).pop();

    row.innerHTML = `
      <td>${fileA} <strong>vs</strong> ${fileB}</td>
      <td>${formatPercent(result.strictSimilarity || result.averageSimilarity)}</td>
      <td><span class="pill ${result.suspicious ? "warn" : "ok"}">${result.suspicious ? "Suspicious" : "Normal"}</span></td>
      <td>${formatPercent(result.scores.ngram)}</td>
      <td>${formatPercent(result.scores.editNormalized)}</td>
    `;

    row.addEventListener("click", () => {
      renderDetail(result);

      [...resultsBody.querySelectorAll("tr")].forEach((r) => {
        r.style.background = "";
      });
      row.style.background = "rgba(43, 115, 255, 0.14)";
    });

    resultsBody.appendChild(row);

    if (index === 0) {
      renderDetail(result);
      row.style.background = "rgba(43, 115, 255, 0.14)";
    }
  });
}

async function analyze() {
  const formData = new FormData();
  selectedFiles.forEach((file) => formData.append("files", file));

  analyzeBtn.disabled = true;
  setStatus("Running plagiarism analysis...");

  try {
    const response = await fetch("/api/analyze", {
      method: "POST",
      body: formData,
    });

    const data = await response.json();

    if (!response.ok) {
      throw new Error(data.error || "Analysis failed.");
    }

    scoreExplanations = data.scoreExplanations || {};
    latestResults = data.results || [];

    updateSummary(data);
    renderTable(latestResults);
    resultsSection.classList.remove("hidden");
    setStatus("Analysis complete. Click a row to inspect details.");
  } catch (error) {
    setStatus(error.message || "Unexpected error.", true);
  } finally {
    analyzeBtn.disabled = selectedFiles.length < 2;
  }
}

function escapeHtml(text) {
  if (text === null || text === undefined) {
    return "";
  }
  return String(text)
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#039;");
}

function handleFiles(fileListLike) {
  selectedFiles = [...fileListLike];
  renderFileList();
}

fileInput.addEventListener("change", (event) => {
  handleFiles(event.target.files);
});

["dragenter", "dragover"].forEach((eventName) => {
  dropzone.addEventListener(eventName, (event) => {
    event.preventDefault();
    dropzone.classList.add("dragover");
  });
});

["dragleave", "drop"].forEach((eventName) => {
  dropzone.addEventListener(eventName, (event) => {
    event.preventDefault();
    dropzone.classList.remove("dragover");
  });
});

dropzone.addEventListener("drop", (event) => {
  handleFiles(event.dataTransfer.files);
});

analyzeBtn.addEventListener("click", analyze);

renderFileList();
