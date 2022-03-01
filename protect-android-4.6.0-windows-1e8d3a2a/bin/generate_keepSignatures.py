#!/usr/bin/env python3
# -*- coding: utf-8 -*-

# usage: generate_keepSignatures.py [-h] [-d DEVICE] -o OUTPUT [-a]
# optional arguments:
#   -h, --help                      show this help message and exit
#   -d DEVICE, --device DEVICE      device id
#   -i INPUT,  --input INPUT        Logcat log as input
#   -o OUTPUT, --output OUTPUT      keepSignatures output file
#   -a, --annotation                Force to collect all annotated classes/methods/field

import argparse
import os
import sys
import re
import subprocess
from time import sleep


def _get_android_sdk_root():
    if "ANDROID_HOME" in os.environ:
        return os.environ.get('ANDROID_HOME')
    elif "ANDROID_SDK_ROOT" in os.environ:
        return os.environ.get('ANDROID_SDK_ROOT')
    raise Exception("Environment variable ANDROID_SDK_ROOT is not set (path to sdk tools, usually ends with /android/sdk)")


ADB_EXEC = os.path.join(_get_android_sdk_root(), "platform-tools", "adb.exe" if sys.platform == "win32" else "adb")
assert os.path.exists(ADB_EXEC), "ADB executable is not found: " + ADB_EXEC


class ClassRule:
    def __init__(self, name):
        self.name = name.rstrip('\n\r')

    def __hash__(self):
        return hash(self.name)

    def __eq__(self, other):
        return isinstance(other, ClassRule) and hash(self) == hash(other)

    def __repr__(self):
        return str(self)

    def __str__(self):
        return self.name


class FieldRule:
    def __init__(self, name, field_type):
        self.name = name.rstrip('\n\r')
        self.field_type = field_type.rstrip('\n\r')

    def __hash__(self):
        return hash((self.name, self.field_type))

    def __eq__(self, other):
        return isinstance(other, FieldRule) and hash(self) == hash(other)

    def __repr__(self):
        return str(self)

    def __str__(self):
        return '%s %s' % (self.field_type, self.name)


class MethodRule:
    def __init__(self, name, method_type, parameters):
        self.name = name.rstrip('\n\r')
        self.method_type = method_type.rstrip('\n\r')
        self.parameters = parameters.rstrip('\n\r')

    def __hash__(self):
        return hash((self.name, self.method_type, self.parameters))

    def __eq__(self, other):
        return isinstance(other, MethodRule) and hash(self) == hash(other)

    def __repr__(self):
        return str(self)

    def __str__(self):
        return '%s %s(%s)' % (self.method_type, self.name, self.parameters)


def _generate_keep_signatures(parsed_output):
    keep_rules = {}
    for reflection_type, name in parsed_output:
        class_name = None
        rule = None
        if reflection_type == "CLASS":
            class_name = name
            rule = ClassRule('*')
        elif reflection_type == "METHOD":
            name_parts = name.split(' ', 1)
            if len(name_parts) >= 2:
                method_type = name_parts[0]
                name = name_parts[1]
                first_bracket_pos = name.find('(')
                last_dot_pos = name[:first_bracket_pos].rfind('.')
                class_name = name[:last_dot_pos]
                method_name = name[last_dot_pos+1:first_bracket_pos]
                parameters = name[first_bracket_pos+1:len(name)-1]
                rule = MethodRule(method_name, method_type, parameters)
        elif reflection_type == "FIELD":
            name_parts = name.split(' ', 1)
            if len(name_parts) >= 2:
                field_type = name_parts[0]
                name = name_parts[1]
                last_dot_pos = name.rfind('.')
                class_name = name[:last_dot_pos]
                field_name = name[last_dot_pos+1:]
                rule = FieldRule(field_name, field_type)

        if rule is not None:
            if class_name in keep_rules:
                keep_rules[class_name].add(rule)
            else:
                keep_rules[class_name] = {rule}
    return keep_rules


def _write_keep_signatures(parsed_rules, output_file):
    rules_count = 0
    try:
        with open(output_file, mode="a", encoding="utf8") as keepSig:
            if os.path.getsize(output_file) > 0:
                keepSig.write("\n")
            keepSig.write("# ---------------- Reflection found by Reflection Debugger ----------------\n")
            for className, data in sorted(parsed_rules.items()):
                if ClassRule('*') in data:
                    keepSig.write('-keep class %s {\n  *;\n}\n' % className)
                else:
                    keepSig.write('-keepclassmembers class %s {\n' % className)
                    for rule in data:
                        keepSig.write('  %s;\n' % str(rule))
                    keepSig.write('}\n')
                rules_count += 1
            keepSig.write("# ------------------------ End of reflection list -------------------------\n")
    except IOError:
        print("Error: failed to access the file: %s." % output_file)
        sys.exit(1)
    return rules_count


def _get_logcat_output(start_time, device):
    print("Updating keepSignatures rules...")
    command = ADB_EXEC + ' -s ' + device + ' logcat -d *:S SECUREDEX'
    if start_time is not None:
        command += ' -t \"' + start_time + '\"'
    process = subprocess.Popen(command, stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)
    output = ''
    while True:
        line = process.stdout.readline().decode('utf-8')
        if not line:
            break
        output += line
    process.terminate()
    return output


def _read_filtered_output_file(input_file):
    try:
        with open(input_file, "r", encoding="utf8") as logcatLog:
            return logcatLog.read()
    except IOError:
        print("Error: failed to read the file: %s." % input_file)
        sys.exit(1)


def _get_filtered_output(output, collect_annotations):
    return re.findall('] (?:POSSIBLE |CALLER %s)?(CLASS|FIELD|METHOD): (.+)' % ('|ANNOTATED ' if collect_annotations else ''), output)


def _get_android_api(device):
    android_api = subprocess.Popen(ADB_EXEC + ' -s ' + device + ' shell getprop ro.build.version.sdk',
                                     stdout=subprocess.PIPE, stderr=subprocess.STDOUT, shell=True)
    sleep(1)
    android_api.terminate()
    return android_api.communicate()[0]


def _clear_adb_logcat(device):
    sleep_proc = subprocess.Popen(ADB_EXEC + ' -s ' + device + ' shell logcat -b main -c', shell=True)
    sleep(1)
    sleep_proc.terminate()


def _resolve_device(device):
    print("Checking devices...")
    check_devices = subprocess.Popen([ADB_EXEC, 'devices'], stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
    device_output = check_devices.communicate()[0].decode('utf-8')

    ids = re.findall('\n(.+)\t', device_output)
    if len(ids) == 0:
        print("Error: no devices found.")
        sys.exit(1)
    else:
        if device is None:
            if len(ids) > 1:
                print("Error: more than one device is attached, consider specifying device id with -d <device id>. Devices found: %s." % (', '.join(ids)))
                sys.exit(1)
            else:
                device = ids[0]
        elif device not in ids:
            print("Error: wrong device id: %s. Devices found: %s." % (device, ', '.join(ids)))
            sys.exit(1)

    return device


def _resolve_device_time(device):
    time_process = subprocess.Popen(ADB_EXEC + ' -s ' + device + ' shell \"date \'+%m-%d %H:%M:%S.000\'\"',
                                    stdout=subprocess.PIPE, stderr=subprocess.STDOUT, shell=True)
    sleep(1)
    time_process.terminate()
    return time_process.communicate()[0].decode('utf-8').strip()


if __name__ == "__main__":
    if not sys.version.startswith('3'):
        print("This script requires python3")
        exit(1)

    parser = argparse.ArgumentParser()
    parser.add_argument("-d", "--device", help="device id")
    parser.add_argument("-i", "--input", help="Logcat log as input")
    parser.add_argument("-o", "--output", help="keepSignatures output file", required=True)
    parser.add_argument('-a', '--annotation', required=False, action='store_true', help='Force to collect all annotated classes/methods/fields')
    args = parser.parse_args()
    parsed_args = vars(args)

    infile = parsed_args["input"]
    outfile = parsed_args["output"]
    collect_annotations = parsed_args["annotation"]
    start_time = None
    api = None

    if infile is None:
        device = _resolve_device(parsed_args["device"])
        try:
            api = _get_android_api(device)
            if int(api) < 21:
                _clear_adb_logcat(device)
            else:
                start_time = _resolve_device_time(device)
        except ValueError:
            print("Error: Android API is not an integer: %s." % (api))
            sys.exit(1)

        print("Colleting reflections... Press Enter to finish.")
        input()

        parsed_output = _get_logcat_output(start_time, device)
    else:
        parsed_output = _read_filtered_output_file(infile)

    filtered_output = _get_filtered_output(parsed_output, collect_annotations)

    keep_rules = _generate_keep_signatures(filtered_output)
    if len(keep_rules) > 0:
        new_rules_count = _write_keep_signatures(keep_rules, outfile)
        print("New keepSignatures rules added: %d" % new_rules_count)
    else:
        print("No rules were generated. "
              "Make sure you have enabled reflection debugger. "
              "It is also possible that reflection debugger didn't find any reflection.")
