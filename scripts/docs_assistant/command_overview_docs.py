import json

from docs_assistant import file_path, command_overview_path
from docs_assistant.git_manager import commit


def command_overview_docs_update():
    content = generate_content()
    open(command_overview_path, 'w').close()
    with open(command_overview_path, 'w') as file:
        file.write(content)        


def generate_content():
    with open(file_path, 'r') as file:
        data = json.load(file)

    contents = "# Commands Overview\n"
    for index, key in enumerate(data.keys()):
        contents += f"{index + 1}. [{key}](commands.md?id={key})\n"

    for key, value in data.items():
        key = str(key)
        output = f"### [{key}]({key.lower()}_commands.md)"
        output += "\n|Name|Description|\n|---|---|\n"
        for command in value:
            output += f"|[`{command['name']}`]({key.lower()}_commands.md?id={command['name']})|{command['description']}|\n"

        contents += f"\n{output}"

    return contents
