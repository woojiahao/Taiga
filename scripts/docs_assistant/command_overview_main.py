import json

from docs_assistant import file_path, main_readme_path

def command_overview_main_update():
    content = generate_content()
    with open(main_readme_path, 'r') as file:
        data = file.read()
    
    start = data.index("<!--blk-1-->\n")
    end = data.index("<!--blk-1-end-->\n")
    before = "".join(data[0:start]) + "<!--blk-1-->\n"
    after = "".join(data[end:])
    final_content = before + content + after
    open(main_readme_path, "w").close()
    with open(main_readme_path, "w") as file:
        file.write(final_content)
         

def generate_content():
    with open(file_path, 'r') as file:
        data = json.load(file)

    contents = ""
    for index, key in enumerate(data.keys()):
        contents += f"{index + 1}. [{key}](https://github.com/woojiahao/Taiga#{key})\n"

    for key, value in data.items():
        key = str(key)
        output = f"### {key}"
        output += "\n|Name|Description|\n|---|---|\n"
        for command in value:
            output += f"|`{command['name']}`|{command['description']}|\n"
        
        contents += output
    
    return contents
    