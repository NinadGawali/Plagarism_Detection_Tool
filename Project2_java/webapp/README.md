# Web Frontend for Plagiarism Detection

This web app provides a frontend + Python API wrapper over your Java plagiarism detection algorithms.

## What it does

- Upload 2 or more code files in the browser.
- Python backend compiles/calls Java logic.
- Results are shown directly in the UI (no terminal menu, no txt-only workflow).

## Run

1. Open terminal in `Project2_java/webapp`
2. Install dependency:

   ```bash
   pip install -r requirements.txt
   ```

3. Start server:

   ```bash
   python app.py
   ```

4. Open browser:

   - http://127.0.0.1:5000

## Notes

- Requires JDK in PATH (`javac` and `java` commands available).
- Java sources are compiled automatically when needed.
- Frontend accepts common code file extensions (`.java`, `.py`, `.cpp`, `.c`, `.js`, `.txt`).
