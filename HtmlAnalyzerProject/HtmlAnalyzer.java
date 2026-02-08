import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Stack;

public class HtmlAnalyzer {
    public static void main(String[] args) {
        if (args.length == 0) return;
        String urlString = args[0];

        try {
            URL url = new URL(urlString);
            BufferedReader reader = new BufferedReader(
            		new InputStreamReader(
            			url.openStream()
            		)
            );
            
            Stack<String> stack = new Stack<>();
            String line;
            String deepestText = null;
            int maxDepth = -1;
            int currentDepth = 0;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                if (isOpeningTag(line)) {
                    stack.push(getTagContent(line));
                    currentDepth++;
                } else if (isClosingTag(line)) {
                    if (
                    	stack.isEmpty() || !stack.pop().equals(getTagContent(line))
                    ) {
                        System.out.println("malformed HTML");
                        return;
                    }
                    currentDepth--;
                } else {
                    if (currentDepth > maxDepth) {
                        maxDepth = currentDepth;
                        deepestText = line;
                    }
                }
            }

            if (!stack.isEmpty()) {
                System.out.println("malformed HTML");
            } else if (deepestText != null) {
                System.out.println(deepestText);
            }

        } catch (Exception e) {
            System.out.println("URL connection error");
        }
    }
    
    // Supports methods for identify scenarios (isOpeningTag, isClosingTag, getTagContent, etc)
    private static boolean isOpeningTag(String line) {
        return line.startsWith("<") && !line.startsWith("</") && line.endsWith(">");
    }

    private static boolean isClosingTag(String line) {
        return line.startsWith("</") && line.endsWith(">");
    }

    private static String getTagContent(String line) {
        if (line.startsWith("</")) {
            return line.substring(2, line.length() - 1);
        }
        return line.substring(1, line.length() - 1);
    }
}
