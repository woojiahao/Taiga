import os
import json

def update_changelog():
    for _, _, files in os.walk("../changelogs"):
        latest = max([int(file[file.index("_") + 1: file.rindex(".")]) for file in files])

    changelog_file = f"../changelogs/changelog_{latest}.json"
    
    with open(changelog_file, "r") as file:
        data = json.load(file)

    content = "# Recent Changes\n"
    content += f"## {data['buildTitle']}\n"
    content += f"Build **{data['buildNumber']}** was released on **{data['releaseDate']}**\n"

    content += "### Changes\n"
    for index, change in enumerate(data["changes"]):
        content += f"{index + 1}. {change}\n"
    
    if (len(data["commands"]) > 0):
        content += "\n### New Commands\n"
        for index, command in enumerate(data["commands"]):
            content += f"{index + 1}. {command}\n"

    content += "\n### Contributors\n"
    for contributor in data["contributors"]:
        content += f"* {contributor}\n"

    open("../docs/recent_changelog.md", "w").close()

    with open("../docs/recent_changelog.md", "w") as file:
        file.write(content)
    
