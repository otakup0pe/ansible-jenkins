#!/usr/bin/env bash
set -e

function problems {
    MSG="$1"
    echo "Problem: ${MSG}"
    exit 1
}

if [ $# != 2 ] ; then
    problems "Invalid args specified"
fi

REPO="$1"
DEST="$2"
REPO_DIR="$(pwd)/.upstream"
if [ -d "$REPO_DIR" ] ; then
    rm -rf "$REPO_DIR"
fi

git clone --depth 1 "$REPO" "$REPO_DIR"

if [ ! -d "$DEST" ] ; then
    mkdir -p "$DEST"
fi

cp "${REPO_DIR}/jenkins/"*.groovy "$DEST"
rm -rf "$REPO_DIR"
