#!/bin/sh
set -eu

task_dir=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
run_dir=$(mktemp -d)
trap 'rm -rf "$run_dir"' EXIT HUP INT TERM

cp "$task_dir/file_four.in" "$run_dir/"
cd "$run_dir"

for test_name in zero one two three four five memory; do
    "$task_dir/rstack_example" "$test_name" > "$test_name.stdout"
    diff -u "$task_dir/file_$test_name.out" "file_$test_name.out"
    printf 'PASS %s\n' "$test_name"
done
