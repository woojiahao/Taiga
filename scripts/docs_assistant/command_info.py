import json
import os

from docs_assistant import file_path, docs_path


def command_info_update():
    with open(file_path, 'r') as file:
        data = json.load(file)

    for key, value in data.items():
        output = f"# {key} Commands\n"
        for command in value:
            output += f"## {command['name']}\n"
            output += "### Description {docsify-ignore}\n"
            output += f"{command['description']}\n"
            output += "### Syntax {docsify-ignore}\n\n"
            output += f"> {command['syntax']}\n\n"
            output += "### Example {docsify-ignore}\n\n"
            output += f"> {command['example']}\n\n"

        documentation_file_path = f"{docs_path}{key.lower()}_commands.md"
        print(documentation_file_path)
        if os.path.isfile(documentation_file_path):
            open(documentation_file_path, "w").close()
        else:
            print(f"Creating {documentation_file_path}")

        with open(documentation_file_path, "w") as docs_file:
            docs_file.write(output)
          