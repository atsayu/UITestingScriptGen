package com.example.demo;

import com.bpodgursky.jbool_expressions.Expression;
import com.bpodgursky.jbool_expressions.Variable;
import com.bpodgursky.jbool_expressions.parsers.ExprParser;
import com.bpodgursky.jbool_expressions.rules.RuleSet;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    @PostMapping("/parse")
    @ResponseBody
    public ResponseEntity<Expression>  parse(@RequestBody Map<String, String> data) {
        try {
            String expr = data.get("expr").toString();
            System.out.println(expr);
            Expression e = ExprParser.parse(expr);
            System.out.println(RuleSet.toDNF(e));
            System.out.println(e.getExprType());
            System.out.println(expressionToJSON(e));
            return new ResponseEntity<>(e, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return handleParseException(e);
        }
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Expression> handleParseException(Exception e) {
        Expression expr = Variable.of("An error occurred while parsing the expression: " + e.getMessage());
        return new ResponseEntity<>(expr, HttpStatus.BAD_REQUEST);
    }

}
