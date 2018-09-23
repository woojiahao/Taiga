import os


def commit(commit_message):
    os.system(f"cd ../ && git commit -m \"{commit_message}\"")
