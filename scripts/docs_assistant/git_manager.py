import os


def commit(commit_message):
    os.system(f"cd ../ && git add . && git commit -m \"{commit_message}\"")
