package invalid;

import objects.action.ClickElement;
import objects.action.InputText;
import objects.assertion.ElementShouldContain;
import objects.assertion.LocationShouldBe;

import java.util.Vector;

import static invalid.DataPreprocessing.*;
import static invalid.InvalidTestGen.getMultiValidKey;
import static invalid.InvalidTestGen.searchValidValue;

public class AssertTestGen {
    static Vector<Vector<Vector<String>>> assertMap = new Vector<>();

    public static Vector<String> initAssertTestGen() {
        Vector<String> finalTest = new Vector<>();
        for (Vector<String> assertVal : assertVec) {
            assertDictParse(assertVal);
        }
        System.out.println("assertMap: " + assertMap);
        for (Vector<Vector<String>> assertVal : assertMap) {
            Vector<String> firstTemp = new Vector<>(temp);
            firstTemp = binaryAssertTrace(firstTemp, assertVal);
            System.out.println("firsttemp: " + firstTemp);
            finalTest.addAll(firstTemp);
        }
        System.out.println("final: " + finalTest);
        return finalTest;
    }

    private static Vector<String> binaryAssertTrace(Vector<String> firstTemp, Vector<Vector<String>> assertVal) {
        Vector<String> binaryTemp;
        Vector<String> invalidTemp = new Vector<>(firstTemp);
        System.out.println(assertVal);
        for (String invalidLine : assertVal.get(1)) {
            invalidTemp = invalidLineAssertTrace(invalidTemp, invalidLine);
            System.out.println(invalidTemp);
        }
        if (!validLineAssertTrace(invalidTemp, assertVal.get(2), assertVal.get(0)).isEmpty()) {
            binaryTemp = validLineAssertTrace(invalidTemp, assertVal.get(2), assertVal.get(0));
        } else {
            binaryTemp = invalidTemp;
        }
        for (int i = 0; i < binaryTemp.size(); i++) {
            if (binaryTemp.get(i).contains("LINE")) {
                Vector<String> lineVal = arrToVec(binaryTemp.get(i).split(" {3}"));
                if (lineVal.get(1).contains("esc")) {
                    ElementShouldContain validVal = new ElementShouldContain(elementShouldContainMap.get(lineVal.get(1)));
                    validVal.setLocator(dataMap.get(validVal.getLocator()).get(0));
                    validVal.setExpected(searchValidValue(lineVal.get(1)));
                    binaryTemp.set(i, "   " + validVal.exprToString());
                } else if (lineVal.get(1).contains("lsb")) {
                    LocationShouldBe validVal = new LocationShouldBe(locationShouldBeMap.get(lineVal.get(1)));
                    validVal.setUrl(searchValidValue(lineVal.get(1)));
                    binaryTemp.set(i, "   " + validVal.exprToString());
                } else if (lineVal.get(1).contains("ce")) {
                    ClickElement validVal = new ClickElement(clickElementMap.get(lineVal.get(1)));
                    validVal.setLocator(searchValidValue(lineVal.get(1)));
                    binaryTemp.set(i, "   " + validVal.exprToString());
                }
            }
        }
        return binaryTemp;
    }

    private static Vector<String> invalidLineAssertTrace(Vector<String> invalidTemp, String invalidLine) {
        Vector<String> lineTemp = new Vector<>(invalidTemp);
        for (String expr : lineDict.get(invalidLine).get(0)) {
            for (int i = 0; i < lineTemp.size(); i++) {
                if (lineTemp.get(i).contains(invalidLine) && lineTemp.get(i).contains(expr)) {
                    if (expr.contains("it")) {
                        InputText multiInvalidIt = new InputText(dataMap.get(inputTextMap.get(expr).getLocator()).get(0), "NOT" + searchValidValue(expr));
                        lineTemp.set(i, "   " + multiInvalidIt.exprToString());
                    } else {
                        ClickElement multiInvalidCe = new ClickElement("NOT" + searchValidValue(expr));
                        lineTemp.set(i, "   " + multiInvalidCe.exprToString());
                    }
                }
            }
        }
        return lineTemp;
    }

    private static Vector<String> validLineAssertTrace(Vector<String> invalidTemp, Vector<String> validLines, Vector<String> assertActionVal) {
        Vector<String> lineTemp = new Vector<>();
        Vector<String> headers = new Vector<>();
        for (String line : validLines) {
            headers.addAll(lineDict.get(line).get(0));
        }
        headers.add(assertActionVal.get(1));
        Vector<String> headersLine = new Vector<>(validLines);
        headersLine.add(assertActionVal.get(0));
        String dataHeader = getMultiValidKey(headers);
        if (dataHeader != null) {
            Vector<String> dataHeaderVec = arrToVec(dataHeader.split(" & "));
            for (String dataString : dataMap.get(dataHeader)) {
                if (!dataString.isEmpty()) {
                    Vector<String> dataVec = arrToVec(dataString.split(" & "));
                    Vector<String> validTemp = new Vector<>(invalidTemp);
                    for (int i = 0; i < headers.size(); i++) {
                        for (int j = 0; j < validTemp.size(); j++) {
                            if (validTemp.get(j).contains(headers.get(i)) && validTemp.get(j).contains(headersLine.get(i))) {
                                if (headers.get(i).contains("it")) {
                                    InputText validVal = new InputText(inputTextMap.get(headers.get(i)));
                                    validVal.setLocator(dataMap.get(validVal.getLocator()).get(0));
                                    validVal.setValue(dataVec.get(dataHeaderVec.indexOf(validVal.getValue())));
                                    validTemp.set(j, "   " + validVal.exprToString());
                                } else if (headers.get(i).contains("lsb")) {
                                    LocationShouldBe validVal = new LocationShouldBe(locationShouldBeMap.get(headers.get(i)));
                                    validVal.setUrl(dataVec.get(dataHeaderVec.indexOf(validVal.getUrl())));
                                    validTemp.set(j, "   " + validVal.exprToString());
                                } else if (headers.get(i).contains("esc")) {
                                    ElementShouldContain validVal = new ElementShouldContain(elementShouldContainMap.get(headers.get(i)));
                                    validVal.setLocator(dataMap.get(validVal.getLocator()).get(0));
                                    validVal.setExpected(dataVec.get(dataHeaderVec.indexOf(validVal.getExpected())));
                                    validTemp.set(j, "   " + validVal.exprToString());
                                }
                            }
                        }
                    }
                    lineTemp.addAll(validTemp);
                }
            }
        }
        return lineTemp;
    }

    private static void assertDictParse(Vector<String> assertVec) {
        Vector<Vector<String>> assertDict = new Vector<>();
        Vector<String> header = new Vector<>(assertVec.subList(0, 2));
        Vector<String> valid = new Vector<>(assertVec.subList(2, assertVec.size()));
        Vector<String> invalid = new Vector<>();
        for (String item : invalidDict.get(0)) {
            if (!valid.contains(item)) {
                invalid.add(item);
            }
        }
        assertDict.add(header);
        assertDict.add(valid);
        assertDict.add(invalid);
        assertMap.add(assertDict);
    }
}
