#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import os
import re
import json
import argparse
import sys

class InliningMapItem():
    def __init__(self, _minLineNumber, _originalLineCount, _inlinedMaxLineNumber, _linesShiftedBy, _inlinedMethods, _inlinedPositions, _methodName):
        self.minLineNumber = _minLineNumber
        self.originalLineCount = _originalLineCount
        self.inlinedMaxLineNumber = _inlinedMaxLineNumber
        self.linesShiftedBy = _linesShiftedBy
        self.inlinedMethods = _inlinedMethods
        self.inlinedPositions = _inlinedPositions
        self.methodName = _methodName


def read_inlining_map(inlining_map_json):
    inlining_map = {}
    for line in inlining_map_json:
        # Inlining map format:
        # each line represents a method with space-separated data
        splits = line.split(' ')
        assert len(splits) >= 7

        id = int(splits[0])                                 # method id

        minLineNumber = int(splits[1])                      # minimal line number
        originalLineCount = int(splits[2])                  # max line number before inlining
        inlinedMaxLineNumber = int(splits[3])               # max line number after inlining
        linesShiftedBy = int(splits[4])                     # the amount all lines were shifted by, used to prevent line collision
        inlinedMethodCount = int(splits[5])                 # count of inlined methods
        inlinedMethods = splits[6:6+inlinedMethodCount]     # a list of inlined method ids
        inlinedMethods = list(map(int, inlinedMethods)) # to integers
        inlinedPositions = splits[6 + inlinedMethodCount:6 + inlinedMethodCount*2]      # a list of positions where methods were inlined
        inlinedPositions = list(map(int, inlinedPositions))  # to integers
        methodName = splits[6+inlinedMethodCount*2].rstrip() # deobfuscated name of the method;  rstrip strips the newline character
        inlining_map[id] = InliningMapItem(minLineNumber, originalLineCount, inlinedMaxLineNumber, linesShiftedBy, inlinedMethods, inlinedPositions, methodName)

    return inlining_map

def retrace_method(inlining_map, crash_method, crash_line):
    if crash_line > inlining_map[crash_method].originalLineCount:
        overflow = crash_line - inlining_map[crash_method].originalLineCount
        # If multiple methods inlined into this method, iterate through them
        # in the same order they were inlined and substract the lines added by it.
        for method, position in zip(inlining_map[crash_method].inlinedMethods, inlining_map[crash_method].inlinedPositions):
            if overflow <= inlining_map[method].inlinedMaxLineNumber:
                # Correct method, go deeper
                retraced_lines = retrace_method(inlining_map, method, overflow)
                # Going up recursion, add current line to result
                current_line = (crash_method, position)
                retraced_lines.append(current_line)
                return retraced_lines
            else:
                # Not this method, remove lines added by this method and move on to next
                overflow = overflow - inlining_map[method].inlinedMaxLineNumber
        return [(-1, -1)] # Error, should not happen
    else:
        # Reached final, uninlined position
        return [(crash_method, crash_line)]

# Finds method ID in the map
def find_initial_method_id(inlining_map, crash_method_name, crash_line):
    for id, inlining_item in inlining_map.items():
        if inlining_item.methodName == crash_method_name:
            itemRangeMin = inlining_item.minLineNumber + inlining_item.linesShiftedBy
            itemRangeMax = inlining_item.inlinedMaxLineNumber + inlining_item.linesShiftedBy
            if crash_line >= itemRangeMin and crash_line <= itemRangeMax:
                return id
    return -1

def needs_deobfuscation(line, start_index, token):
    tmp_start_index = start_index-1
    tmp_end_index = start_index+len(token)
        
    #check for spaces before and after token
    if tmp_start_index >= 0 and tmp_end_index < len(line):
        if line[tmp_start_index] == ' ' and line[tmp_end_index] == ' ':
            return False

    return True

def safe_replace(line, token, match):
    # deobfuscate all words that are more than 3 characters length
    if len(token) > 3:
        return line.replace(token, match)

    start_index = 0
    end_index = len(line)
        
    
    # indexes that point to token index that will be replaced by match (deobfuscated)
    replace_indexes = [];

    while start_index != -1 and start_index < end_index:
        start_index = line.find(token, start_index, end_index)
        
        if start_index != -1:
            if needs_deobfuscation(line, start_index, token):
                replace_indexes.append(start_index)

            # increase index to skip already processed token
            start_index += len(token)

    if len(replace_indexes) > 0:
        new_string = ""
        start_index = 0
        
        #create new dobfuscated string
        while start_index < end_index:
            if start_index in replace_indexes:
                # character is token, add deobfuscated value to new string
                # increase index to skip token
                new_string += match
                start_index += len(token)
            else:
                # character is not token, just copy it to new string
                new_string += line[start_index]
                start_index += 1

        return new_string

    return line

def try_deobfuscate_line(line, merged_map, inlining_map):
    # Try to de-obfuscate simple methods and class like strings
    tokens = re.split(r'[`\-=~!@#%^&*()+\[\]{};\'\\:"|/? ]', line)
    for token in tokens:
        token = token.rstrip('\n')
        match = merged_map.get(token)

        if match is not None:
            line = safe_replace(line, token, match)

    # Try to de-obfuscate inlined methods
    deobfuscated_inlines = []
    stackLine = re.search("(\s)at.*\(.*\:[0-9]+\)", line) # matches " at com.my.class.method(class.java:102)"
    if stackLine is not None:
        try:
            crash_method_name_pos = (line.rfind('at ') + 3, line.rfind('('))
            crash_method_name = line[crash_method_name_pos[0]:crash_method_name_pos[1]]
            crash_line_pos = (line.rfind(':')+1, line.rfind(')'))
            crash_line_number = int(line[crash_line_pos[0]:crash_line_pos[1]])

            base_crash_method_id = find_initial_method_id(inlining_map, crash_method_name, crash_line_number)
            if base_crash_method_id != -1:
                result = retrace_method(inlining_map, base_crash_method_id, crash_line_number)
                for result_line in result:
                    deobfuscated_method_name = inlining_map[result_line[0]].methodName
                    deobfuscated_crash_line_number = result_line[1]
                    # File name
                    deobfuscated_class_name = deobfuscated_method_name[:deobfuscated_method_name.rfind('.')]
                    deobfuscated_file_name = deobfuscated_class_name[deobfuscated_class_name.rfind('.') + 1:]

                    deobfuscated_stack_line = line[:crash_method_name_pos[0]] + deobfuscated_method_name + "(" + deobfuscated_file_name + ":" + str(deobfuscated_crash_line_number) + line[crash_line_pos[1]:]
                    deobfuscated_inlines.append(deobfuscated_stack_line)
        except Exception:
            deobfuscated_inlines.append("Error, the following line was not deobfuscated:\n")
            deobfuscated_inlines.append(line)
            pass


    lines = [line]
    if len(deobfuscated_inlines) > 0:
        lines = deobfuscated_inlines

    for i in range(len(lines)):
        lines[i] = lines[i].replace("_VirtualWrapper", "_ControlFlowFlatteningHelper")
        lines[i] = lines[i].replace("_StaticWrapper", "_ControlFlowFlatteningHelper")
        lines[i] = lines[i].replace("_ProxyWrapper", "_ControlFlowFlatteningHelper")

    return lines


def deobfuscate(stack_trace, output, deob_map_json):
    if sys.version_info[0] < 3:
        raise Exception("This script requires python version 3.x")

    merged_map = {}
    inlining_map_json = {}

    with open(deob_map_json, 'r', encoding="utf8") as deob_map_f:
        deob_map = json.loads(deob_map_f.read())
        classes = deob_map.get('classes')
        for entry in classes:
            for obfuscated, original in entry.items():
                original_name = original.split(':')[0]
                obfuscated_name = obfuscated.split(':')[0]
                merged_map[obfuscated_name] = original_name

                class_name_original = original_name[original_name.rfind('.') + 1:]
                class_name_obfuscated = obfuscated_name[obfuscated_name.rfind('.') + 1:]
                merged_map[class_name_obfuscated] = class_name_original

        methods = deob_map.get('methods')
        for entry in methods:
            for obfuscated, original in entry.items():
                original_name = original.split(':')[0]
                obfuscated_name = obfuscated.split(':')[0]
                merged_map[obfuscated_name] = original_name

        inlining_map_json = deob_map.get('inliningMap')

    stack_trace_f = open(stack_trace, 'r', encoding="utf8", errors='ignore') if stack_trace is not None else sys.stdin
    stack_trace_lines = [line for line in stack_trace_f]

    inlining_map = read_inlining_map(inlining_map_json)

    stack_trace_lines_deobfuscated = []
    for line in stack_trace_lines:
        stack_trace_lines_deobfuscated.extend(try_deobfuscate_line(line, merged_map, inlining_map))

    for line in stack_trace_lines_deobfuscated:
        output.write(line)

def main():
    parser = argparse.ArgumentParser(description='Digital.ai Android protection de-obfuscation tool')
    parser.add_argument('-s', '--stack-trace', required=False, action='store', help='Stack trace file. The trace is read from stdin if not provided')
    parser.add_argument('-d', '--deobfuscation-map', required=False, action='store', default='DeobfuscationMap.json', help='De-obfuscation map JSON file. Defaults to DeobfuscationMap.json')
    parser.add_argument('-o', '--output', required=False, action='store', help='Output file. Output is written to stdout if not provided')
    args = parser.parse_args()

    output = open(args.output, 'w', encoding="utf8") if args.output is not None else sys.stdout

    deobfuscate(args.stack_trace, output, args.deobfuscation_map)


if __name__ == '__main__':
    main()
    exit(0)
