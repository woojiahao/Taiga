import datetime
import json
import os


def generate_changelog():
    for root, dirs, files in os.walk("../changelogs"):
        latest = max([int(file[file.index("_") + 1: file.rindex(".")]) for file in files])

    changelog_file = f"../changelogs/changelog_{latest + 1}.json"
    contents = {
        "buildNumber": "",
        "buildTitle": "Improvements",
        "changes": [],
        "releaseDate": f"{datetime.datetime.now().strftime('%d %B %Y')}",
        "contributors": []
    }
    with open(changelog_file, "w") as file:
        json.dump(contents, file)

    print(f"{changelog_file} has been created")


if __name__ == '__main__':
    generate_changelog()
