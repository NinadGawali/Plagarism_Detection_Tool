from __future__ import annotations

import json
import subprocess
import tempfile
from pathlib import Path
from typing import List

from flask import Flask, jsonify, request, send_from_directory

app = Flask(__name__, static_folder="static", static_url_path="")

PROJECT_ROOT = Path(__file__).resolve().parent.parent
SRC_DIR = PROJECT_ROOT / "src"
BIN_DIR = PROJECT_ROOT / "bin"


@app.route("/")
def index():
    return send_from_directory(app.static_folder, "index.html")


@app.route("/api/analyze", methods=["POST"])
def analyze_files():
    uploaded = request.files.getlist("files")

    if len(uploaded) < 2:
        return jsonify({"error": "Upload at least 2 files."}), 400

    try:
        ensure_java_compiled()
    except RuntimeError as exc:
        return jsonify({"error": str(exc)}), 500

    with tempfile.TemporaryDirectory(prefix="plagiarism_upload_") as temp_dir:
        temp_path = Path(temp_dir)
        saved_files: List[Path] = []

        for file_storage in uploaded:
            if not file_storage.filename:
                continue

            destination = temp_path / Path(file_storage.filename).name
            file_storage.save(destination)
            saved_files.append(destination)

        if len(saved_files) < 2:
            return jsonify({"error": "Valid files were not received."}), 400

        command = [
            "java",
            "-cp",
            str(BIN_DIR),
            "Project2_java.src.WebComparisonRunner",
            *[str(path) for path in saved_files],
        ]

        try:
            result = subprocess.run(
                command,
                capture_output=True,
                text=True,
                cwd=str(PROJECT_ROOT),
                check=True,
            )
        except FileNotFoundError:
            return jsonify({"error": "Java runtime was not found in PATH."}), 500
        except subprocess.CalledProcessError as exc:
            message = (exc.stderr or exc.stdout or "Unknown Java execution error").strip()
            return jsonify({"error": f"Java comparison failed: {message}"}), 500

        try:
            payload = json.loads(result.stdout)
        except json.JSONDecodeError:
            return jsonify({"error": "Invalid response received from Java runner."}), 500

        payload["inputFiles"] = [path.name for path in saved_files]
        return jsonify(payload)


@app.route("/api/health", methods=["GET"])
def health():
    return jsonify({"status": "ok"})


def ensure_java_compiled() -> None:
    java_sources = [
        SRC_DIR / "Main.java",
        SRC_DIR / "WebComparisonRunner.java",
        *sorted((SRC_DIR / "similarity").glob("*.java")),
        *sorted((SRC_DIR / "algo").glob("*.java")),
    ]

    if not java_sources:
        raise RuntimeError("Java source files were not found.")

    BIN_DIR.mkdir(exist_ok=True)

    class_file = BIN_DIR / "Project2_java" / "src" / "WebComparisonRunner.class"
    needs_compile = (not class_file.exists()) or any(
        source.stat().st_mtime > class_file.stat().st_mtime for source in java_sources
    )

    if not needs_compile:
        return

    javac_cmd = [
        "javac",
        "-encoding",
        "UTF-8",
        "-d",
        str(BIN_DIR),
        *[str(source) for source in java_sources],
    ]

    try:
        subprocess.run(
            javac_cmd,
            capture_output=True,
            text=True,
            cwd=str(PROJECT_ROOT),
            check=True,
        )
    except FileNotFoundError:
        raise RuntimeError("javac was not found in PATH. Install JDK 17+ and retry.")
    except subprocess.CalledProcessError as exc:
        error_output = (exc.stderr or exc.stdout or "Compilation failed").strip()
        raise RuntimeError(f"Java compilation failed: {error_output}")


if __name__ == "__main__":
    app.run(host="127.0.0.1", port=5000, debug=True)
