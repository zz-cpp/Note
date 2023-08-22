import fs from 'fs';
import path from 'path';

/**
 * Words example: TS implementation with map/filter/reduce
 */

/**
 * Find all files in the filesystem subtree rooted at folder.
 * @param folder root of subtree, requires folder.isDirectory() == true
 * @returns stream of all ordinary files (not folders) that have folder as
 *          their ancestor
 */
function allFilesIn(folder: string): Array<string> {
    const children: Array<string> = fs.readdirSync(folder)
                                    .map(f => path.join(folder, f));
    const descendants: Array<string> = children
                                       .filter(f => fs.lstatSync(f).isDirectory())
                                       .map(allFilesIn)
                                       .flat();
    return [
        ...descendants,
        ...children.filter(f => fs.lstatSync(f).isFile())
    ];
}

/**
 * Make a filename suffix testing predicate.
 * @param suffix string to test
 * @returns a predicate that returns true iff the filename ends with suffix.
 */
function endsWith(suffix: string): (filename: string) => boolean {
    return (filename: string) => filename.endsWith(suffix);
}

const filenames: Array<string> = allFilesIn(".")
                                 .filter(s => s.endsWith(".ts"));
const fileContents: Array<Array<string>> = filenames.map(f => {
    const data = fs.readFileSync(f, { encoding: "utf8", flag: "r" });
    return data.split(/\r?\n/);
});
const lines: Array<string> = fileContents.flat();
const words: Array<string> = lines.map(line => line.split(/\W+/)
                                               .filter(s => s.length > 0))
                             .flat();
words.forEach(s => console.log(s));
