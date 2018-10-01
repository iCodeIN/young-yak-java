import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class SRTFile {

    private List<SRTEntry> entries = new ArrayList<>();

    public SRTFile(File f) throws FileNotFoundException {

        Scanner scanner = new Scanner(f);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            SRTEntry entry = new SRTEntry();

            // process number
            if (line.matches("[0-9]+")) {
                entry.number = Integer.parseInt(line);
                line = scanner.hasNextLine() ? scanner.nextLine() : "";
            } else {
                continue;
            }

            // process time
            if (line.matches("[0-9][0-9]:[0-9][0-9]:[0-9][0-9],[0-9][0-9][0-9] --> [0-9][0-9]:[0-9][0-9]:[0-9][0-9],[0-9][0-9][0-9]")) {
                entry.beginTime = line.split(" --> ")[0];
                entry.endTime = line.split(" --> ")[1];
                line = scanner.hasNextLine() ? scanner.nextLine() : "";
            } else {
                continue;
            }

            // process content
            entry.text = "";
            while (!line.isEmpty()) {
                entry.text += (line + "\n");
                line = scanner.hasNextLine() ? scanner.nextLine() : "";
            }
            entry.text = entry.text.isEmpty() ? "" : entry.text.substring(0, entry.text.length() - 1);

            // add entry
            entries.add(entry);
        }
        scanner.close();
    }

    public static List<File> getFiles(File root) {
        List<File> srtFiles = new ArrayList<>();
        Stack<File> tmp = new Stack<File>();
        tmp.push(root);
        while (!tmp.isEmpty()) {
            File f = tmp.pop();
            if (f.isFile() && f.getName().endsWith(".srt"))
                srtFiles.add(f);
            if (f.isDirectory()) {
                for (File sub : f.listFiles()) {
                    tmp.push(sub);
                }
            }
        }
        return srtFiles;
    }

    public List<SRTEntry> getEntries() {
        return entries;
    }

    static class SRTEntry {
        private int number;
        private String beginTime;
        private String endTime;
        private String text;

        public int getNumber() {
            return number;
        }

        public String getText() {
            return text;
        }

    }
}
