package invalid;
import invalid.strategies.Context;
import objects.assertion.LocationAssertion;
import objects.normalAction.ClickElement;
import objects.normalAction.InputText;
import java.util.*;
import static invalid.DataPreprocessing.*;
import static invalid.strategies.Context.isAssertion;
import static invalid.strategies.Context.isDynamic;

public class AssertTestGen {
    static HashMap<String, Vector<Vector<String>>> assertStructureMap = new HashMap<>();
    static Context searchValidValueContext = new Context();

    public static Vector<String> assertTestGenInit() {
        assertStructureMapInit();
        return invalidTestNumbering(invalidAssertTestGen());
    }

    private static Vector<String> invalidTestNumbering(Vector<String> finalTest) {
        int index = 1;
        for (int i = 0; i < finalTest.size(); i++) {
            if (finalTest.get(i).contains("Invalid-Test")) {
                finalTest.set(i, finalTest.get(i) + "-" + index);
                index++;
            }
        }
        return finalTest;
    }

    private static Vector<String> invalidAssertTestGen() {
        Vector<String> finalScript = new Vector<>();
        Vector<String> assertKeyVec = new Vector<>(assertStructureMap.keySet());
        Collections.sort(assertKeyVec);
        for (String key : assertKeyVec) {
            Vector<String> assertTemp = new Vector<>(temp);
            for (int i = 0; i < assertTemp.size(); i++) {
                if (assertTemp.get(i).contains("LINE") && assertTemp.get(i).substring(0, 5).compareTo(key) > 0) {
                    assertTemp.set(i, "");
                }
            }
            assertTemp.removeIf(String::isBlank);
            Vector<String> finalTest = new Vector<>();
            if (!assertStructureMap.get(key).get(0).isEmpty()) {
                finalTest = assertValidTestTrace(assertTemp, assertStructureMap.get(key).get(0));
            }
            finalTest = assertInvalidDataFill(finalTest, assertStructureMap.get(key).get(1));
            assertStaticFill(finalTest, assertStructureMap.get(key));
            finalScript.addAll(finalTest);
        }
        return finalScript;
    }

    private static void assertStaticFill(Vector<String> assertTemp, Vector<Vector<String>> assertMap) {
        for (int i = 0; i < assertTemp.size(); i++) {
            if (assertTemp.get(i).contains("LINE")) {
                Vector<String> lineVal = arrToVec(assertTemp.get(i).split("\t"));
                if (!isDynamic(lineVal.get(1))) {
                    if (assertMap.get(0).contains(lineVal.get(0))) {
                        if (lineVal.get(1).contains("ce")) {
                            ClickElement validAction = new ClickElement(clickElementMap.get(lineVal.get(1)));
                            validAction.setElementLocator(dataMap.get(validAction.getElementLocator()).get(0));
                            assertTemp.set(i, "\t" + validAction.exprToString());
                        }
                    } else if (assertMap.get(1).contains(lineVal.get(0))) {
                        assertTemp.set(i, "#\t" + assertTemp.get(i));
                    }
                }
            }
        }
        assertTemp.removeIf(String::isBlank);
    }

    private static Vector<String> assertValidTestTrace(Vector<String> assertTemp, Vector<String> validLineVec) {
        Vector<Vector<String>> headers = new Vector<>();
        Vector<String> finalTest = new Vector<>();
        initHeaders(headers, validLineVec);
        for (Vector<String> header : headers) {
            Vector<String> headerKeys = findHeaderKey(header);
            for (String headerKey : headerKeys) {
                Vector<String> headerKeyVec = arrToVec(headerKey.split(" & "));
                headerKeyVec.replaceAll(String::trim);
                for (String validVal : dataMap.get(headerKey)) {
                    if (!validVal.isBlank()) {
                        Vector<String> assertTempWithData = new Vector<>(assertTemp);
                        finalTest.addAll(assertValidDataFill(assertTempWithData, validVal, validLineVec, headerKeyVec));
                    }
                }
            }
        }
        return finalTest;
    }

    private static void scriptDataFill (Vector<String> scriptTemp, int i, Vector<String> lineVal, Vector<String> headerKeyVec, Vector<String> validValVec) {
        String actionVal = searchValidValueContext.searchValidValue(lineVal.get(1));
        int actionValIndex = headerKeyVec.indexOf(actionVal);
        if(actionValIndex == -1) {
            if (isDynamic(lineVal.get(1))) {
                scriptTemp.set(i, "");
            }
        } else {
            if(lineVal.get(1).contains("it")) {
                InputText validAction = new InputText(inputTextMap.get(lineVal.get(1)));
                validAction.setElementLocator(dataMap.get(validAction.getElementLocator()).get(0));
                validAction.setValue(validValVec.get(actionValIndex));
                scriptTemp.set(i, "\t" + validAction.exprToString());
            } else if (lineVal.get(1).contains("la")) {
                LocationAssertion validAction = new LocationAssertion(locationShouldBeMap.get(lineVal.get(1)));
                validAction.setUrl(validValVec.get(actionValIndex));
                scriptTemp.set(i, "\t" + validAction.exprToString());
            }
        }
    }

    private static Vector<String> assertValidDataFill (Vector<String> scriptTemp, String validVal, Vector<String> validLineVec, Vector<String> headerKeyVec) {
        Vector<String> validValVec = arrToVec(validVal.split(" & "));
        validValVec.replaceAll(String::trim);
        for (int i = 0; i < scriptTemp.size(); i++) {
            if (scriptTemp.get(i).contains("LINE")) {
                Vector<String> lineVal = arrToVec(scriptTemp.get(i).split("\t"));
                lineVal.replaceAll(String::trim);
                if (validLineVec.contains(lineVal.get(0))) {
                    scriptDataFill(scriptTemp, i, lineVal, headerKeyVec, validValVec);
                }
            }
        }
        scriptTemp.removeIf(String::isBlank);
        return scriptTemp;
    }

    private static Vector<String> findHeaderKey(Vector<String> header) {
        Vector<String> headerKey = new Vector<>();
        for (String key : dataMap.keySet()) {
            Vector<String> keyVal = arrToVec(key.split(" & "));
            if (containsVector(keyVal, header)) {
                headerKey.add(key);
            }
        }
        return headerKey;
    }

    public static boolean containsVector(Vector<String> vector1, Vector<String> vector2) {
        for (String str : vector2) {
            if (!vector1.contains(str)) {
                return false;
            }
        }
        return true;
    }

    private static void initHeaders(Vector<Vector<String>> headers, Vector<String> validLineVec) {
        headers.add(new Vector<>());
        for (String validLine : validLineVec) {
            boolean firstCheck = true;
            Vector<Vector<String>> prevHeaders = new Vector<>();
            for (Vector<String> header : headers) {
                prevHeaders.add(new Vector<>(header));
            }
            for (Vector<Vector<String>> lineVec : dnfLineDict.get(validLine)) {
                headersValidLineValueFill(headers, prevHeaders, firstCheck, lineVec);
                if (firstCheck) {
                    firstCheck = false;
                }
            }
        }
    }

    private static void headersValidLineValueFill(
            Vector<Vector<String>> headers,
            Vector<Vector<String>> prevHeaders,
            boolean firstCheck,
            Vector<Vector<String>> lineVec) {
        if (firstCheck) {
            for (Vector<String> header : headers) {
                for (String action : lineVec.get(0)) {
                    if (isDynamic(action)) {
                        header.add(searchValidValueContext.searchValidValue(action));
                    }
                }
            }
        } else {
            Vector<Vector<String>> tempHeaders = new Vector<>(prevHeaders);
            for (Vector<String> header : tempHeaders) {
                for (String action : lineVec.get(0)) {
                    if (isDynamic(action)) {
                        header.add(searchValidValueContext.searchValidValue(action));
                    }
                }
            }
            headers.addAll(tempHeaders);
        }
    }

    private static Vector<String> assertInvalidDataFill(Vector<String> finalTemp, Vector<String> invalidLineVec) {
        Vector<String> finalTest = new Vector<>();
        for (String line : invalidLineVec) {
            for (Vector<Vector<String>> lineVec : cnfLineDict.get(line)) {
                Vector<String> assertTemp = new Vector<>(finalTemp);
                for (int i = 0; i < assertTemp.size(); i++) {
                    if (assertTemp.get(i).contains("LINE")) {
                        Vector<String> lineVal = arrToVec(assertTemp.get(i).split("\t"));
                        if (lineVal.get(0).equals(line)) {
                            if (lineVec.get(0).contains(lineVal.get(1))) {
                                invalidDataFill(lineVal, assertTemp, i);
                            } else {
                                assertTemp.set(i, "");
                            }
                        } else if (invalidLineVec.contains(lineVal.get(0))) {
                            invalidValidDataFill(lineVal, assertTemp, i);
                        }
                    }
                }
                assertTemp.removeIf(String::isBlank);
                finalTest.addAll(assertTemp);
            }
        }
        return finalTest;
    }

    private static void invalidDataFill(Vector<String> lineVal, Vector<String> scriptTemp, int i) {
        if(lineVal.get(1).contains("it")) {
            InputText invalidAction = new InputText(inputTextMap.get(lineVal.get(1)));
            invalidAction.setElementLocator(dataMap.get(invalidAction.getElementLocator()).get(0));
            invalidAction.setValue("NOT_" + getValidValue(searchValidValueContext.searchValidValue(lineVal.get(1))));
            scriptTemp.set(i, "\t" + invalidAction.exprToString());
        } else if (lineVal.get(1).contains("ce")) {
            ClickElement validAction = new ClickElement(clickElementMap.get(lineVal.get(1)));
            validAction.setElementLocator(dataMap.get(validAction.getElementLocator()).get(0));
            scriptTemp.set(i, "#\t" + validAction.exprToString());
        }
    }

    private static void invalidValidDataFill(Vector<String> lineVal, Vector<String> scriptTemp, int i) {
        if(lineVal.get(1).contains("it")) {
            InputText validAction = new InputText(inputTextMap.get(lineVal.get(1)));
            validAction.setElementLocator(dataMap.get(validAction.getElementLocator()).get(0));
            validAction.setValue(getValidValue(searchValidValueContext.searchValidValue(lineVal.get(1))));
            scriptTemp.set(i, "\t" + validAction.exprToString());
        } else if (lineVal.get(1).contains("ce")) {
            ClickElement validAction = new ClickElement(clickElementMap.get(lineVal.get(1)));
            validAction.setElementLocator(dataMap.get(validAction.getElementLocator()).get(0));
            scriptTemp.set(i, "\t" + validAction.exprToString());
        }
    }

    private static void assertStructureMapInit() {
        int i = 1;
        Vector<String> assertValidHeap = new Vector<>();
        Vector<String> assertInvalidHeap = new Vector<>();
        while (dnfLineDict.get("LINE" + i) != null) {
            Vector<Vector<Vector<String>>> lineVal = dnfLineDict.get("LINE" + i);
            if (isAssertion(lineVal.get(0).get(0).get(0))) {
                assertValidHeap.add("LINE" + i);
                Vector<Vector<String>> assertMap = new Vector<>();
                assertMap.add(new Vector<>(assertValidHeap));
                assertMap.add(new Vector<>(assertInvalidHeap));
                assertStructureMap.put("LINE" + i, assertMap);
                assertValidHeap.addAll(assertInvalidHeap);
                assertInvalidHeap.clear();
            } else {
                assertInvalidHeap.add("LINE" + i);
            }
            i++;
        }
    }

    static String getMultiValidKey(Vector<String> header) {
        String keyVal = null;
        Context getMultiValidKeyContext = new Context();
        Vector<String> headerKey = getMultiValidKeyContext.getMultiValid(header);
        for (String key : dataMap.keySet()) {
            Vector<String> keyVec = arrToVec(key.split(" & "));
            if (sameElement(keyVec, headerKey)) {
                keyVal = key;
                break;
            }
        }
        return keyVal;
    }

    public static boolean sameElement(Vector<String> v1, Vector<String> v2) {
        Vector<String> a1 = new Vector<>(v1);
        Vector<String> a2 = new Vector<>(v2);
        Collections.sort(a1);
        Collections.sort(a2);
        return a1.equals(a2);
    }

    public static Vector<Vector<String>> getHeader(Vector<String> header, String value) {
        Vector<String> valueVec = arrToVec(value.split(" "));
        Vector<String> headerVec = new Vector<>();
        Vector<String> invalidVec = new Vector<>();
        for (int i = 0; i < header.size(); i++) {
            if (valueVec.get(i).equals("1")) {
                headerVec.add(header.get(i));
            } else {
                invalidVec.add(header.get(i));
            }
        }
        Vector<Vector<String>> tbVec = new Vector<>();
        tbVec.add(new Vector<>(headerVec));
        tbVec.add(new Vector<>(invalidVec));
        return tbVec;
    }

    public static String getValidValue(String value) {
        String valueKey = null;
        int index = 0;
        for (String key : dataMap.keySet()) {
            Vector<String> keyVec = arrToVec(key.split(" & "));
            if (keyVec.contains(value)) {
                valueKey = key;
                index = keyVec.indexOf(value);
                break;
            }
        }
        Vector<String> valueVec = arrToVec(dataMap.get(valueKey).get(0).split(" & "));
        return valueVec.get(index);
    }
}