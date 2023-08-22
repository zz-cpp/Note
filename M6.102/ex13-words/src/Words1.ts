import fs from 'fs';
import path from 'path';

/**
 * Words example: familiar TS implementation
 */

/**
 * Find names of all files in the filesystem subtree rooted at folder.
 * @param folder root of subtree, requires fs.lstatSync(folder).isDirectory() === true
 * @returns array of names of all ordinary files (not folders) that have folder as
 *          their ancestor
 */
function allFilesIn(folder: string): Array<string> {
    let files: Array<string> = [];
    for (const child of fs.readdirSync(folder)) {
        const fullNameOfChild = path.join(folder, child);
        if (fs.lstatSync(fullNameOfChild).isDirectory()) {
            files = files.concat(allFilesIn(fullNameOfChild));
        } else if (fs.lstatSync(fullNameOfChild).isFile()) {
            files.push(fullNameOfChild);
        }
    }
    return files;
}

/**
 * Filter an array of files to those that end with suffix.
 * @param filenames array of filenames
 * @param suffix string to test
 * @returns a new array consisting of only those files whose names end with suffix
 */
function onlyFilesWithSuffix(filenames: Array<string>, suffix: string): Array<string> {
    const result: Array<string> = [];
    for (const f of filenames) {
        if (f.endsWith(suffix)) {
            result.push(f);
        }
    }
    return result;
}

/**
 * Find all the non-word-character-separated words in files.
 * @param filenames list of files (all non-null)
 * @returns list of words from all the files
 * @throws IOException if an error occurs reading a file
 */
function getWords(filenames: Array<string>): Array<string> {
    const words: Array<string> = [];
    for (const f of filenames) {
        const data = fs.readFileSync(f, { encoding: "utf8", flag: "r" });
        const lines = data.split(/\r?\n/);
        for (const line of lines) {
            // split on \W (non-word characters, like spaces and punctuation)
            for (const word of line.split(/\W+/)) {
                // split can return empty strings, so omit them
                if (word.length > 0) {
                    words.push(word);
                }
            }
        }
    }
    return words;
}


const allFiles = allFilesIn(".");
const tsFiles = onlyFilesWithSuffix(allFiles, ".ts");
const words = getWords(tsFiles);
for (const s of words) { console.log(s); }
