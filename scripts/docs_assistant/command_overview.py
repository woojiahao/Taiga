import json

file_path = '../config/help.json'


def command_overview_update():
    with open(file_path, 'r') as file:
        data = json.load(file)

    for key, value in data.items():
        output = f"## {key}\n"
        for index, command in enumerate(value, 1):
            output += f"{index}. [{command['name']}]({key}_commands.md?id={command['name']})\n"

        output += "\n"
        for command in value:
            output += f"## {command['name']}\n"
            output += "### Description {docsify-ignore}\n"
            output += f"{command['description']}\n"
            output += "### Syntax {docsify-ignore}\n\n"
            output += f"> {command['syntax']}\n\n"
            output += "### Example {docsify-ignore}\n\n"
            output += f"> {command['example']}\n\n"

        print(output)
