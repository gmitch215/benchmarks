#!/bin/bash

original_dir="$1"
modified_dir="$2"

if [[ ! -d "$original_dir" || ! -d "$modified_dir" ]]; then
  echo "Both directories must exist"
  exit 1
fi

find "$modified_dir" -type f | while read modified_file; do
  relative_path="${modified_file#$modified_dir/}"

  original_file="$original_dir/$relative_path"

  if [[ -f "$original_file" && -f "$modified_file" ]]; then
    if [[ $(md5sum "$original_file" | cut -d ' ' -f 1) == $(md5sum "$modified_file" | cut -d ' ' -f 1) ]]; then
      echo "Removing identical file: $modified_file"
      rm "$modified_file"
    fi
  fi

  find "$modified_dir" -type d -empty | while read dir; do
    echo "Removing empty directory: $dir"
    rmdir "$dir"
  done
done