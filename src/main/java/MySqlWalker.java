import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

/**
 * Created by WSYOU on 2017-12-29.
 */
public class MySqlWalker extends MySqlParserBaseVisitor<Integer> {
    // MAIN함수
    public static void main(String[] args) throws IOException {

        String resDir = "src/main/resources";
        String filePath = resDir + "/" + args[0];
        System.out.println("FileName : " + filePath);
        MySqlLexer lexer = new MySqlLexer( new ANTLRFileStream(filePath));
        MySqlParser parser = new MySqlParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.root();
        Integer answer = new MySqlWalker().visit(tree);
        System.out.println( readLineByLineJava8( filePath ));
        System.out.printf("%s\n", answer);
    }
    //Read file content into string with - Files.lines(Path path, Charset cs)
    private static String readLineByLineJava8(String filePath)
    {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8))
        {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return contentBuilder.toString();
    }
}
