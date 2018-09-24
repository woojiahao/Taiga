import datetime
import json
import sys


def generate_changelog(name):
    changelog_file = f"../changelogs/changelog_{name}.json"
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
    if len(sys.argv) < 2:
        print("Pass in the only name of the changelog")
    else:
        generate_changelog(sys.argv[1])
