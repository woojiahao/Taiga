import json

file_path = '../config/help.json'

with open(file_path, 'r') as file:
	data = json.load(file)

contents = ""
for index, key in enumerate(data.keys()):
	contents += f"{index + 1}. [{key}](commands.md?id={key})\n"
print(contents)

for key, value in data.items():
	key = str(key)
	output = f"### [{key}]({key.lower()}_commands.md)"
	output += "\n|Name|Description|\n|---|---|\n"
	for command in value:
		output += f"|[`{command['name']}`]({key.lower()}_commands.md?id={command['name']})|{command['description']}|\n"

	print(output)
