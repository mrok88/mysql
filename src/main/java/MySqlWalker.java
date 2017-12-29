import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.util.HashMap;
/**
 * Created by WSYOU on 2017-12-29.
 */
public class MySqlWalker extends MySqlParserBaseVisitor<Integer> {
    HashMap<String,Integer> map = new HashMap<String,Integer>();

    @Override
    public Integer visitTableName( MySqlParser.TableNameContext ctx) {
        String tblNm ;
        tblNm = ctx.getText();
        push(tblNm);
        System.out.println(ctx.getText());
        return this.visit(ctx.fullId());
    }
    // 추출된 테이블을 Unique하게 하기위해서 hashmap에 " 테이블명 : 참조횟수 " 로 기록한다.
    public void push(String key){
        Integer value = map.get(key);
        if (value != null) {
            // key 가 있고 찾아졌음. 그러면 참고횟수를 증가시낀다.
            value += 1;
            map.put(key,value);
        } else {
            // Key might be present...
            if (map.containsKey(key)) {
                // Okay, there's a key but the value is null
                //value 초기값은 0으로 함으로 null이면 오류임.
                System.out.println("map value error ");
            } else {
                // Definitely no such key
                map.put(key,1);
            }
        }
    }
    // map에 저장된 테이블명과 참조횟수를 출력한다.
    public void printMap() {
        for( String key : map.keySet() ){
            System.out.println( String.format("키 : %s, 값 : %s", key, map.get(key)) );
        }
    }
    // MAIN함수
    public static void main(String[] args) throws IOException {

        String resDir = "src/main/resources";
        String filePath = resDir + "/" + args[0];
        System.out.println("FileName : " + filePath);
        MySqlLexer lexer = new MySqlLexer( new ANTLRFileStream(filePath));
        MySqlParser parser = new MySqlParser(new CommonTokenStream(lexer));
        ParseTree tree = parser.root();
        System.out.println( readLineByLineJava8( filePath ));
        System.out.println("============== TableList ==============");
        MySqlWalker msw = new MySqlWalker();
        Integer answer = msw.visit(tree);
        System.out.println("============== Map TableList ==============");
        msw.printMap();
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
