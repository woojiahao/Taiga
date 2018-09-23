import json

file_path = '../config/help.json'

with open(file_path, 'r') as file:
    data = json.load(file)

contents = ""
for index, key in enumerate(data.keys()):
    contents += f"{index + 1}. [{key}](https://github.com/woojiahao/Taiga#{key})\n"
print(contents)

for key, value in data.items():
    key = str(key)
    output = f"### {key}"
    output += "\n|Name|Description|\n|---|---|\n"
    for command in value:
        output += f"|`{command['name']}`|{command['description']}|\n"

    print(output)
