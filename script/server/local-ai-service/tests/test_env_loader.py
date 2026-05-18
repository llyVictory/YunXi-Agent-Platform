import os
import tempfile
import unittest
from pathlib import Path
from unittest.mock import patch

from env_loader import load_local_dotenv


class LoadLocalDotenvTest(unittest.TestCase):
    def test_loads_env_file_without_manual_source(self) -> None:
        with tempfile.TemporaryDirectory() as temp_dir:
            temp_path = Path(temp_dir)
            (temp_path / "env_loader.py").write_text("", encoding="utf-8")
            (temp_path / ".env").write_text(
                "EMBEDDING_MODEL=./models/from-dotenv\n",
                encoding="utf-8",
            )

            with patch("env_loader.__file__", str(temp_path / "env_loader.py")):
                os.environ.pop("EMBEDDING_MODEL", None)
                load_local_dotenv()

                self.assertEqual(
                    "./models/from-dotenv",
                    os.getenv("EMBEDDING_MODEL"),
                )


if __name__ == "__main__":
    unittest.main()
