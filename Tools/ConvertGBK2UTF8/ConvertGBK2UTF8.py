# coding: utf-8


import os
import sys


def convert_file(path):
    """Covert the encode of fire from GBK to UTF8"""
    try:
        with open(path) as f:
            i = f.read()

        o = i.decode('GBK').encode('UTF8')
        with open(path, 'w') as f:
            f.write(o)

        print('"' + path + '" Converted')
    except Exception as e:
        print('"' + path + '" Convert Failed!')
        print(e)


folder = sys.path[0]
filters = ['.java', '.xml']

info = 'We will search all files under folder "' + folder + '",\n'\
    + 'Filter the files whose extension is ' + ', '.join(filters) + ',\n'\
    + 'Then covert them from GBK to UTF8.\n'\
    + 'Are you sure? (Y/N)'
if raw_input(info).lower() != 'y':
    exit(0)

# Traversal files
for d, _, files in os.walk(folder):
    for i in files:
        # Skip myself
        if d == folder and i == __file__:
            continue

        # Filter by extension
        _, ext = os.path.splitext(i)
        if ext in filters:
            convert_file(d + os.sep + i)

