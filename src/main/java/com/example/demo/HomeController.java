package com.example.demo;

import mockpage.Input;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
import com.bpodgursky.jbool_expressions.And;
import com.bpodgursky.jbool_expressions.Expression;
import com.bpodgursky.jbool_expressions.Or;
import com.bpodgursky.jbool_expressions.Variable;
import com.bpodgursky.jbool_expressions.parsers.ExprParser;
import mockpage.newSolve;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import valid.ScriptGen;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;


@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    public JSONObject expressionToJSON(Expression e) {
        if (e.getExprType().equals("variable")) {
            String[] arr = e.toString().split(" ");
            if (arr[0].equals("'Click")) {
                String type = "Click Element";
                String locator = arr[arr.length - 1];
                JSONObject json = new JSONObject();
                json.put("type", type);
                json.put("locator", locator);
                return json;
            } else if (arr[0].equals("'Input")) {
                String type = "Input text";
                String locator = arr[arr.length - 1];
                String text = "valid_".concat(locator);
                JSONObject json = new JSONObject();
                json.put("type", type);
                json.put("locator", locator);
                json.put("text", text);
                return json;
            }
        }
        else {
            JSONObject json = new JSONObject();
            JSONArray children = new JSONArray();
            List<Expression> childExpr = e.getChildren();
            for (Expression expr: childExpr) {
                children.put(expressionToJSON(expr));
            }
            json.put("type", e.getExprType());
            json.put("LogicofActions", children);
            return json;
        }
        return null;
    }
    public String createAction(Expression e) {
        if (e.getExprType() == "variable") {
            return e.toLexicographicString();
        }
        List<Expression> expressionList = e.getChildren();
        StringBuilder str = new StringBuilder();
        if (e.getExprType() == "or") str.append("OR(");
        else if (e.getExprType() == "and") str.append("AND(");
        for (int i = 0; i < expressionList.size(); i++) {
            str.append(createAction(expressionList.get(i))).append(", ");
        }
        str.append(")");
        return str.toString();
    }

    public static String preprocess(String expr) {
        StringBuilder str = new StringBuilder(expr);
        String modified = expr.replaceAll("[)(]", " ").trim();
        return modified;
    }

    public static Expression postProcessing(Expression input, String[] arr) {
        if (input.getExprType().equals("variable")) {
            int index = Integer.parseInt(input.toString());
            Expression newExpr = Variable.of(arr[index]);
            return newExpr;
        } else {
            List<Expression<String>> children = input.getChildren();
            List<Expression<String>> newChildren = new ArrayList<>();
            for (Expression expression: children) {
                newChildren.add(postProcessing(expression, arr));
            }
            System.out.println(newChildren);
            if (input.getExprType().equals("and")) return And.of(newChildren);
            else if (input.getExprType().equals("or")) return Or.of(newChildren);
            return null;
        }
    }
    @PostMapping("/parse")
    @ResponseBody
    public ResponseEntity<List<Expression>>  parse(@RequestBody Map<String, String> data) {
        try {
            String input = data.get("expr");
            String[] exprList = input.split(",");
            List<Expression> list = new ArrayList<>();
            for (String action : exprList) {
                StringBuilder expr = new StringBuilder(action.trim());
                Map<String, Integer> map = new HashMap<>();
                String[] arr = expr.toString().split("[&|]");

                for (int i = 0; i < arr.length; i++) {
                    arr[i] = preprocess(arr[i]);
                    if (!map.containsKey(arr[i])) map.put(arr[i], i);
                    int start= expr.indexOf(arr[i]);
                    int end = start + arr[i].length();
                    expr.replace(start, end, map.get(arr[i]).toString());
                }
                Expression e = ExprParser.parse(expr.toString());
                Expression newExpression = postProcessing(e, arr);
                System.out.println(newExpression);
                list.add(newExpression);
            }


            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @PostMapping("/testtemplate")
    public ResponseEntity<String> template(@RequestBody Map<String, String> data) throws IOException, ParserConfigurationException, SAXException {
        String xml = data.get("template");
        System.out.println(xml);


        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
        Document document = builder.parse(inputStream);
        Map<String, String> map = new HashMap<>();
        String url = document.getElementsByTagName("url").item(0).getTextContent();
        List<String> locators = new ArrayList<>();
//        locators.add(url);
        NodeList actions = document.getElementsByTagName("LogicExpressionOfActions");
        for (int i = 0; i < actions.getLength(); i++) {
            if (actions.item(i).getNodeType() != Node.ELEMENT_NODE) continue;
            Element action = (Element) actions.item(i);
            String type = action.getElementsByTagName("type").item(0).getTextContent();
            if (action.getElementsByTagName("locator").getLength() != 0) {
                String locator = action.getElementsByTagName("locator").item(0).getTextContent();
                if (!locators.contains(locator) && type.equals("Input Text")) locators.add(locator);
                if (type.equals("Input Text")) {
                    String text = action.getElementsByTagName("text").item(0).getTextContent();
                    if (!map.containsKey(locator)) map.put(locator, text);
                }
            }

        }
        String[] locatorsInput = new String[locators.size()];
        locatorsInput = locators.toArray(locatorsInput);
        System.out.println(locatorsInput);
        System.out.println(map);
        File template = new File ("src/main/resources/template/outline.xml");
        if (template.createNewFile()) {
            System.out.println("Created template file");
        } else {
            System.out.println("File existed");
        }
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(template));
        bufferedWriter.append(xml);
        bufferedWriter.close();
//        try {
//            newSolve.changDomAndCreateMockPage(urlAndLocators, map);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        Input ip = new Input();
        ip.changeDomAndCreateMockPageVersion2("src/main/resources/template/outline.xml");


        /* Sửa lại như sau: mapLocatorVariableAndValueVariable là map ngay trên,chỉ lấy locatorsInput do chỉ nhập data ở các phần tử input
        Input ip = new Input();
        try {
          ip.changeDomAndCreateMockPage(locatorsInput, url, mapLocatorVariableAndValueVariable);
        } catch (IOException e) {
          e.printStackTrace();
        }
         */




        ScriptGen.createDataSheetV2("src/main/resources/template/outline.xml", "src/main/resources/data/datasheet.csv");
        return new ResponseEntity<>("Mocked the page", HttpStatus.OK);
    }

    @PostMapping("/createtest")
    public ResponseEntity<String> createScript(@RequestBody Map<String, String> body) throws IOException {

        String csvData = body.get("values");
        File data = new File ("src/main/resources/data/data.csv");
        if (data.createNewFile()) {
            System.out.println("Created template file");
        } else {
            System.out.println("File existed");
        }
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(data));
        bufferedWriter.append(csvData);
        bufferedWriter.close();

        newSolve.fillInCSV("src/main/resources/data/datasheet.csv","src/main/resources/data/data.csv", newSolve.getDataFromCSV("src/main/resources/data/data.csv"));

        ScriptGen.createScriptV2("src/main/resources/template/outline.xml", "src/main/resources/data/data.csv", "src/main/resources/robot_test_file/final_test.robot");


        return new ResponseEntity<>("Create Script", HttpStatus.OK);
    }

    public void createScriptHtml() throws IOException {
        File robotFile = new File("src/main/resources/testscript/script.robot");
        File htmlFile = new File ("src/main/resources/templates/script.html");

        BufferedReader bufferedReader = new BufferedReader(new FileReader(robotFile));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(htmlFile));

        String line = bufferedReader.readLine();
        while (line != null) {
            bufferedWriter.append(line).append("\n");
            line = bufferedReader.readLine();
        }

        bufferedWriter.close();
        bufferedReader.close();
    }

    @GetMapping("/test.html")
    public String test(Model model) throws IOException {
        StringBuilder mockWebContent = new StringBuilder();
        File mockWebFile = new File("src/main/resources/html/mockweb.html");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(mockWebFile));
        File headContent = new File("src/main/resources/html/head.html");
        BufferedReader headReader = new BufferedReader(new FileReader(headContent));
        StringBuilder headString = new StringBuilder();

        String line = bufferedReader.readLine();
        while (line != null) {
            mockWebContent.append(line).append("\n");
            line = bufferedReader.readLine();
        }
        bufferedReader.close();

        String headLine = headReader.readLine();
        while (headLine != null) {
            headString.append(headLine).append("\n");
            headLine = headReader.readLine();
        }
        headReader.close();
        model.addAttribute("webContent", mockWebContent.toString());
        System.out.println(headReader.toString());
        model.addAttribute("headContent", headString.toString());
        return "test";
    }

    @GetMapping("/script")
    public String sendScript( Model model) throws IOException {
        StringBuilder testScript = new StringBuilder();
        File robotFile = new File("src/main/resources/robot_test_file/final_test.robot");

        BufferedReader bufferedReader = new BufferedReader(new FileReader(robotFile));

        String line = bufferedReader.readLine();
        while (line != null) {
            testScript.append(line).append("\n");
            line = bufferedReader.readLine();
        }
        bufferedReader.close();
        model.addAttribute("testScript", testScript.toString());
        return "script";
    }

    private void runRobotScript(SseEmitter emitter) throws IOException {

            String outputDirectory = "src/main/resources/html";
            ProcessBuilder processBuilder = new ProcessBuilder("robot","--outputdir", outputDirectory, "src/main/resources/robot_test_file/final_test.robot");
            processBuilder.redirectErrorStream(true);
            // Set working directory if necessary
            // processBuilder.directory(new File("path/to/your/script"));

            Process process = processBuilder.start();
            // Capture the output stream
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Users")) line = line.split("\\\\")[line.split("\\\\").length - 1];
                // Send each line of output to the client
                emitter.send(line, MediaType.TEXT_PLAIN);
            }
        } catch (IOException e) {
            emitter.completeWithError(e);
        } finally {
            int exitCode;
            try {
                exitCode = process.waitFor();
            } catch (InterruptedException e) {
                emitter.completeWithError(e);
                return;
            }

            // Send the exit code to the client
            emitter.send("Exit Code: " + exitCode, MediaType.TEXT_PLAIN);
            emitter.complete();
        }
    }

    @GetMapping("/download-robot-report")
    public ResponseEntity<InputStreamResource> downloadReport() throws IOException {
        File reportFile = new File("src/main/resources/html/report.html");
        InputStreamResource inputStreamResource = new InputStreamResource(new FileInputStream(reportFile));
//        byte[] reportFileContent = Files.readAllBytes(new ClassPathResource("src/main/resources/html/report.html").getFile().toPath());
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=report.html")
                .body(inputStreamResource);
    }

    @GetMapping("/download-robot-log")
    public ResponseEntity<InputStreamResource> downloadLog() throws IOException {
        File reportFile = new File("src/main/resources/html/log.html");
        InputStreamResource inputStreamResource = new InputStreamResource(new FileInputStream(reportFile));
//        byte[] reportFileContent = Files.readAllBytes(new ClassPathResource("src/main/resources/html/report.html").getFile().toPath());
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=log.html")
                .body(inputStreamResource);
    }
    @GetMapping("/run-script-robot")
    public SseEmitter runRobotFramework() {
        System.out.println("Run");
        SseEmitter emitter = new SseEmitter();

        CompletableFuture.runAsync(() -> {
            try {
                runRobotScript(emitter);
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        });

        return emitter;
    }
    @GetMapping("/report-robot")
    public String viewRobotReport() {
        return "Final Test Log";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Expression> handleParseException(Exception e) {
        Expression expr = Variable.of("An error occurred while parsing the expression: " + e.getMessage());
        return new ResponseEntity<>(expr, HttpStatus.BAD_REQUEST);
    }

}
