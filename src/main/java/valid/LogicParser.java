package valid;

import com.bpodgursky.jbool_expressions.And;
import com.bpodgursky.jbool_expressions.Expression;
import com.bpodgursky.jbool_expressions.Or;
import com.bpodgursky.jbool_expressions.Variable;

import com.bpodgursky.jbool_expressions.rules.RuleSet;
import objects.normalAction.*;
import objects.assertion.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LogicParser {

    public static Expression<String> createTextExpression(JSONObject actionJSON) {
        String type = actionJSON.get("type").toString();
        if (!type.equals("and") && !type.equals("or") && !type.equals("open")) {
            String text = null;
            if (type.equals("input")) {
                text = actionJSON.get("value").toString();
            }
            return Variable.of(text);
        }
        if (type.equals("and")) {
//            NodeList childNodes = element.getChildNodes();
//            List<Element> childElements = new ArrayList<>();
//            List<String> childString = new ArrayList<>();
//            for (int i = 0; i < childNodes.getLength(); i++) {
//                Node child = childNodes.item(i);
//                if (child.getNodeType() == Node.ELEMENT_NODE && ((Element) child).getTagName().equals("LogicExpressionOfActions"))
//                    childElements.add((Element) child);
//            }
//            for (int i = 0; i < childElements.size(); i++) {
//                childString.add(childElements.get(i).getElementsByTagName("text").item(0).getTextContent());
//            }
//            Expression[] expressionList = new Expression[childElements.size()];
//            for (int i = 0; i < childElements.size(); i++) {
//                expressionList[i] = createTextExpression(childElements.get(i));
//            }
            JSONArray actions = (JSONArray) actionJSON.get("actions");
            Expression[] expressionList = new Expression[actions.size()];
            for (int i = 0; i < actions.size(); i++) {
                JSONObject childActionJSON = (JSONObject) actions.get(i);
                expressionList[i] = createTextExpression(childActionJSON);
            }

            Expression expression = And.of(expressionList, Expression.LEXICOGRAPHIC_COMPARATOR);
            return expression;

        } else if (type.equals("or")) {
//            NodeList childNodes = element.getChildNodes();
//            List<Element> childElements = new ArrayList<>();
//
//
//            for (int i = 0; i < childNodes.getLength(); i++) {
//                Node child = childNodes.item(i);
//                if (child.getNodeType() == Node.ELEMENT_NODE && ((Element) child).getTagName().equals("LogicExpressionOfActions"))
//                    childElements.add((Element) child);
//            }
//
//            Expression[] expressionList = new Expression[childElements.size()];
//            for (int i = 0; i < childElements.size(); i++) {
//                expressionList[i] = createTextExpression(childElements.get(i));
//            }
            JSONArray actions = (JSONArray) actionJSON.get("actions");
            Expression[] expressionList = new Expression[actions.size()];
            for (int i = 0; i < actions.size(); i++) {
                JSONObject childActionJSON = (JSONObject) actions.get(i);
                expressionList[i] = createTextExpression(childActionJSON);
            }
            Expression expression = Or.of(expressionList, Expression.LEXICOGRAPHIC_COMPARATOR);
            return expression;
        }
        System.out.println("Wrong type of logic expression!");
        return null;
    }

    public static Expression<objects.Expression> createAction(JSONObject actionJSON) {
        String type = actionJSON.get("type").toString();
//        boolean required = Boolean.parseBoolean(element.getElementsByTagName("required").item(0).getTextContent());
        boolean required = false;
        if (!type.equals("and") && !type.equals("or")) {
            switch (type) {
                case "input": {
                    String describedLocator = actionJSON.get("describedLocator").toString();
                    String value = actionJSON.get("value").toString();
                    return Variable.of(new InputText(describedLocator, value, true, required));
                }
                case "click": {
                    String describedLocator = actionJSON.get("describedLocator").toString();
                    return Variable.of(new ClickElement(describedLocator, false, required));
                }
                case "verifyURL": {
                    String expectedURL = actionJSON.get("url").toString();
                    return Variable.of(new LocationAssertion(expectedURL));
                }
//                case "Select Checkbox": {
//                    String locator = element.getElementsByTagName("answer").item(0).getTextContent();
//                    return Variable.of(new SelectCheckbox(locator));
//                }
//                case "Select Radio Button": {
//                    String groupName = element.getElementsByTagName("question").item(0).getTextContent();
//                    String value = element.getElementsByTagName("choice").item(0).getTextContent();
//                    return Variable.of(new SelectRadioButton(groupName, value, true, required));
//                }
//                case "Select List":
//                    String elementLocator = element.getElementsByTagName("list").item(0).getTextContent();
//                    String value = element.getElementsByTagName("value").item(0).getTextContent();
//                    System.out.println("Create Select list");
//                    return Variable.of(new SelectList(elementLocator, value, true, required));
//                case "Verify URL":
//                    String urlVariable = element.getElementsByTagName("url").item(0).getTextContent();
//                    return Variable.of(new LocationAssertion(urlVariable));
            }
//            String text;
//
//            if (type.equals("Input Text")) text = element.getElementsByTagName("text").item(0).getTextContent();
//            else text = null;
//            return Variable.of(action);
        }
        if (type.equals("and")) {

            JSONArray actions = (JSONArray) actionJSON.get("actions");
            Expression[] expressionList = new Expression[actions.size()];
            for (int i = 0; i < actions.size(); i++) {
                JSONObject childActionJSON = (JSONObject) actions.get(i);
                expressionList[i] = createAction(childActionJSON);
            }

            Expression expression = And.of(expressionList, Expression.LEXICOGRAPHIC_COMPARATOR);
            return expression;

        } else if (type.equals("or")) {
            JSONArray actions = (JSONArray) actionJSON.get("actions");
            Expression[] expressionList = new Expression[actions.size()];
            for (int i = 0; i < actions.size(); i++) {
                JSONObject childActionJSON = (JSONObject) actions.get(i);
                expressionList[i] = createAction(childActionJSON);
            }
            Expression expression = Or.of(expressionList, Expression.LEXICOGRAPHIC_COMPARATOR);
            return expression;
        }
        System.out.println("Wrong type of logic expression!");
        return null;
    }

    public static Expression<JSONObject> createActionV2(JSONObject actionJSON) {
        String type = actionJSON.get("type").toString();
        if (!type.equals("and") && !type.equals("or")) {
            return Variable.of(new JSONObject(actionJSON));
        } else if (type.equals("and")) {

            JSONArray actions = (JSONArray) actionJSON.get("actions");
            Expression[] expressionList = new Expression[actions.size()];
            for (int i = 0; i < actions.size(); i++) {
                JSONObject childActionJSON = (JSONObject) actions.get(i);
                expressionList[i] = createActionV2(childActionJSON);
            }

            Expression expression = And.of(expressionList, Expression.LEXICOGRAPHIC_COMPARATOR);
            return expression;

        } else if (type.equals("or")) {
            JSONArray actions = (JSONArray) actionJSON.get("actions");
            Expression[] expressionList = new Expression[actions.size()];
            for (int i = 0; i < actions.size(); i++) {
                JSONObject childActionJSON = (JSONObject) actions.get(i);
                expressionList[i] = createActionV2(childActionJSON);
            }
            Expression expression = Or.of(expressionList, Expression.LEXICOGRAPHIC_COMPARATOR);
            return expression;
        }
        System.out.println("Wrong type of logic expression!");
        return null;
    }

    public static List<List<JSONObject>> createDNFListV2(Expression expr) {
        expr = RuleSet.toDNF(expr);
        List<List<JSONObject>> list = new ArrayList<>();
        if (expr.getExprType().equals("and")) {
            List<JSONObject> childOfAnd = expr.getAllK().stream().toList();
            ArrayList<JSONObject> sortable = new ArrayList<>(childOfAnd);
            Collections.sort(sortable, new Comparator<JSONObject>() {
                @Override
                public int compare(JSONObject o1, JSONObject o2) {
                    return o1.get("type").toString().compareTo(o2.get("type").toString());
                }
            });
            list.add(sortable);
            return list;
        } else if (expr.getExprType().equals("or")) {
            List<Expression> expressionList = expr.getChildren();
//        List<Expression> expressionList = expr.getAllK().stream().toList();
            for (Expression expression : expressionList) {
                List<Expression> andExpressions = expression.getChildren();
                List<JSONObject> andOfActions = new ArrayList<>();
                for (Expression e : andExpressions) {
                    andOfActions.add((JSONObject) e.getAllK().stream().toList().get(0));
                }
                if (expression.getExprType().equals("variable"))
                    andOfActions.add((JSONObject) expression.getAllK().stream().toList().get(0));
                list.add(andOfActions);
            }
        } else {
            JSONObject singleAction = (JSONObject) expr.getAllK().stream().toList().get(0);
            List<JSONObject> tempList = new ArrayList<>();
            tempList.add(singleAction);
            list.add(tempList);
        }

//        List<Expression> expressionList = expr.getChildren();
//        List<Expression> expressionList = expr.getAllK().stream().toList();
//        for (Expression expression : expressionList) {
//            List<Expression> andExpressions = expression.getChildren();
//            List<JSONObject> andOfActions = new ArrayList<>();
//            for (Expression e : andExpressions) {
//                andOfActions.add((JSONObject) e.getAllK().stream().toList().get(0));
//            }
//            if (expression.getExprType().equals("variable"))
//                andOfActions.add((JSONObject) expression.getAllK().stream().toList().get(0));
//            list.add(andOfActions);
//        }


        return list;
    }


    public static List<List<objects.Expression>> createDNFList(Expression expr) {
        expr = RuleSet.toDNF(expr);
        List<List<objects.Expression>> list = new ArrayList<>();
        if (expr.getExprType().equals("and")) {
            List<objects.Expression> childOfAnd = expr.getAllK().stream().toList();
            ArrayList<objects.Expression> sortable = new ArrayList<>(childOfAnd);
            Collections.sort(sortable);
            list.add(sortable);
            return list;
        }

        List<Expression> expressionList = expr.getChildren();
        for (Expression expression : expressionList) {
            List<Expression> andExpressions = expression.getChildren();
            List<objects.Expression> andOfActions = new ArrayList<>();
            for (Expression e : andExpressions) {
                andOfActions.add((objects.Expression) e.getAllK().stream().toList().get(0));
            }
            if (expression.getExprType().equals("variable"))
                andOfActions.add((objects.Expression) expression.getAllK().stream().toList().get(0));
            list.add(andOfActions);
        }
        return list;
    }
}
