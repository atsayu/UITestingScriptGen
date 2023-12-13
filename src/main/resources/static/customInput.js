

// JavaScript program to convert
// infix to prefix.

// Function to check if
// given character is
// an operator or not.
function isOperator(c) {
    return (!(c >= 'a' && c <= 'z') &&
        !(c >= '0' && c <= '9') &&
        !(c >= 'A' && c <= 'Z') && c !== " ");
}

// Function to find priority
// of given operator.
function getPriority(C) {
    if (C == '-' || C == '|')
        return 1;
    else if (C == '&' || C == '/')
        return 2;
    else if (C == '^')
        return 3;
    return 0;
}

// Function that converts infix
// expression to prefix expression.
function infixToPrefix(infix) {
    // stack for operators.
    let operators = [];

    // stack for operands.
    let operands = [];

    for (let i = 0; i < infix.length; i++) {

        // If current character is an
        // opening bracket, then
        // push into the operators stack.
        if (infix[i] == '(') {
            operators.push(infix[i]);
        }

        // If current character is a
        // closing bracket, then pop from
        // both stacks and push result
        // in operands stack until
        // matching opening bracket is
        // not found.
        else if (infix[i] == ')') {
            while (operators.length != 0 &&
                operators[operators.length - 1] != '(') {

                // operand 1
                let op1 = operands.pop();


                // operand 2
                let op2 = operands.pop();


                // operator
                let op = operators.pop();


                // Add operands and operator
                // in form operator +
                // operand1 + operand2.
                let tmp = op + op2 + op1;
                operands.push(tmp);
            }

            // Pop opening bracket
            // from stack.
            operators.pop();
        }

        // If current character is an
        // operand then push it into
        // operands stack.
        else if (!isOperator(infix[i])) {
            let operand = "";
            while (i < infix.length && !isOperator(infix[i])) {
                operand = operand.concat(infix[i]);
                i++;
            }
            operand = '(' + operand + ')';
            operands.push(operand);
            i--;
        }

        // If current character is an
        // operator, then push it into
        // operators stack after popping
        // high priority operators from
        // operators stack and pushing
        // result in operands stack.
        else {
            while (operators.length &&
                getPriority(infix[i]) <=
                getPriority(operators[operators.length - 1])) {

                let op1 = operands.pop();


                let op2 = operands.pop();


                let op = operators.pop();


                let tmp = op + op2 + op1;
                operands.push(tmp);
            }

            operators.push(infix[i]);
        }
    }

    // Pop operators from operators
    // stack until it is empty and
    // operation in add result of
    // each pop operands stack.
    while (operators.length != 0) {
        let op1 = operands.pop();


        let op2 = operands.pop();


        let op = operators.pop();


        let tmp = op + op2 + op1;
        operands.push(tmp);
    }

    // Final prefix expression is
    // present in operands stack.
    return operands[operands.length - 1];
}

function splitPrefix(prefix) {
    let arr = [];
    for (let i = 0; i < prefix.length; i++) {
        if (prefix[i] == "&" || prefix[i] == "|") arr.push(prefix[i]);
        else if (prefix[i] === '(') {
            let operand = "";
            i++
            while (i < prefix.length && prefix[i] !== ')') {
                operand = operand.concat(prefix[i]);
                i++;
            }
            arr.push(operand);
        }
    }
    return arr.reverse();
}

function parseToAction(arr) {
    let st = [];
    for (let i = 0; i < arr.length; i++) {
        if (arr[i] !== '&' && arr[i] !== '|') {
            st.push(arr[i]);
        } else {
            let sec = st.pop();
            let first = st.pop();
            st.push(arr[i] + "(" + first + ',' + sec + ")");
        }
    }
    return st.pop();
}

// Driver code
// let s = "Click 1 & Click 2 & Click 3";
// console.log(infixToPrefix(s));
// console.log(splitPrefix(infixToPrefix(s)));
// console.log(parseToAction(splitPrefix(infixToPrefix(s))));

function createFillClick(locator) {
    let clickElement = `<li><div class="action" type="click">
    <input type="checkbox" name="select" onclick="selecting(this)">
    Click
    <input type="text" placeholder="locator" value="${locator}">
</div></li>`;
    console.log(clickElement)
    return clickElement;
}

function createFillInput(locator, text) {
    let clickElement = `<li><div class="action" type="input">
    <input type="checkbox" name="select" onclick="selecting(this)">
    Input
    <input type="text"  placeholder="text" value="${text}">
    to
    <input type="text" placeholder="locator" value="${locator}">
</div></li>`;
    console.log(clickElement)
    return clickElement;
}



function createFromValue(value) {
    value = value.replaceAll("'", "");
    let arr= value.split(' ');
    console.log(arr);
    if (arr[0] === "Click") {
        return createFillClick(arr[arr.length - 1]);
    } else if (arr[0] === "Input") {
        return createFillInput(arr[arr.length - 1], "valid_" + arr[arr.length -1]);
    }
}

function jsonToElement(json, layer) {
    let type = json.exprType;
    console.log(type);
    if (type === 'variable') {
        console.log("var")
        return createFromValue(json.value);
    } else if (type === 'or') {
        let orElement = `<li><div class = "action" type="or">
    <input type="checkbox" name="select" onclick="selecting(this)">
    <span class="caret" onclick="showAndHide(this)"></span>
    <div class="btn-group dropend">
        <button type="button" class="btn btn-secondary dropdown-toggle btn-sm" data-bs-toggle="dropdown"
            aria-expanded="false">
            Combined actions
        </button>
        <ul class="dropdown-menu">
            <li><a class="dropdown-item" onclick="createCustom(this)">Describe your actions</a></li>
                    <li><a class="dropdown-item" onclick="createAnd(this)">And of actions</a></li>
                    <li><a class="dropdown-item" onclick="createOr(this)">Or of actions</a></li>
                    <li><a class="dropdown-item" onclick="createClick(this)">Click</a></li>
                    <li><a class="dropdown-item" onclick="createInputText(this)">InputText</a></li>
                    <li><a class="dropdown-item" onclick="createSelectList(this)">Select list</a></li>
                    <li><a class="dropdown-item" onclick="createRadioChoice(this)">Radio Button</a></li>
                    <li><a class="dropdown-item" onclick="createCheckbox(this)">Check box</a></li>
                    <li><a class="dropdown-item" onclick="createAcceptPopUp(this)">Accept popup</a></li>
                    <li><a class="dropdown-item" onclick="createCancelPopUp(this)">Cancel popup</a></li>
                    <li><a class="dropdown-item" onclick="createInputToPopUp(this)">Input to popup</a></li>
            </ul>
    </div>
    <ol class="actions nested active">
  `;
        let footer = `                </ol>
</div>
</li>`
            if (layer === 1) {
                orElement = `<li><div class = "action" type="or">
    <input type="checkbox" name="select" onclick="selecting(this)">
    <div class="btn-group dropend">
        <button type="button" class="btn btn-secondary dropdown-toggle btn-sm" data-bs-toggle="dropdown"
            aria-expanded="false">
            Combined actions
        </button>
        <ul class="dropdown-menu">
            <li><a class="dropdown-item" onclick="createCustom(this)">Describe your actions</a></li>
                    <li><a class="dropdown-item" onclick="createAnd(this)">And of actions</a></li>
                    <li><a class="dropdown-item" onclick="createOr(this)">Or of actions</a></li>
                    <li><a class="dropdown-item" onclick="createClick(this)">Click</a></li>
                    <li><a class="dropdown-item" onclick="createInputText(this)">InputText</a></li>
                    <li><a class="dropdown-item" onclick="createSelectList(this)">Select list</a></li>
                    <li><a class="dropdown-item" onclick="createRadioChoice(this)">Radio Button</a></li>
                    <li><a class="dropdown-item" onclick="createCheckbox(this)">Check box</a></li>
                    <li><a class="dropdown-item" onclick="createAcceptPopUp(this)">Accept popup</a></li>
                    <li><a class="dropdown-item" onclick="createCancelPopUp(this)">Cancel popup</a></li>
                    <li><a class="dropdown-item" onclick="createInputToPopUp(this)">Input to popup</a></li>
            </ul>
    </div>
    <ol class="actions nested active">
  `;
            }
        ;

        let jsonArr = json.children;
        jsonArr.forEach(function(item) {
            orElement = orElement.concat(jsonToElement(item, layer + 1)) ;
        });
        orElement = orElement.concat(footer);
        return orElement;
    } else if (type === 'and') {
        let andElement = `<li><div class = "action" type="and">

    <input type="checkbox" name="select" onclick="selecting(this)">
    <span class="caret" onclick="showAndHide(this)"></span>
    <div class="btn-group dropend">
        <button type="button" class="btn btn-secondary dropdown-toggle btn-sm" data-bs-toggle="dropdown"
            aria-expanded="false">
            Combined actions
        </button>
        <ul class="dropdown-menu">
            <li><a class="dropdown-item" onclick="createCustom(this)">Describe your actions</a></li>
                    <li><a class="dropdown-item" onclick="createAnd(this)">And of actions</a></li>
                    <li><a class="dropdown-item" onclick="createOr(this)">Or of actions</a></li>
                    <li><a class="dropdown-item" onclick="createClick(this)">Click</a></li>
                    <li><a class="dropdown-item" onclick="createInputText(this)">InputText</a></li>
                    <li><a class="dropdown-item" onclick="createSelectList(this)">Select list</a></li>
                    <li><a class="dropdown-item" onclick="createRadioChoice(this)">Radio Button</a></li>
                    <li><a class="dropdown-item" onclick="createCheckbox(this)">Check box</a></li>
                    <li><a class="dropdown-item" onclick="createAcceptPopUp(this)">Accept popup</a></li>
                    <li><a class="dropdown-item" onclick="createCancelPopUp(this)">Cancel popup</a></li>
                    <li><a class="dropdown-item" onclick="createInputToPopUp(this)">Input to popup</a></li>
            </ul>
    </div>
    <ol class="actions nested active">`;

        let footer = `                </ol>
</div>
</li>
`;
        if (layer === 1) {
            andElement  =`<li><div class = "action" type="and">

    <input type="checkbox" name="select" onclick="selecting(this)">
    <div class="btn-group dropend">
        <button type="button" class="btn btn-secondary dropdown-toggle btn-sm" data-bs-toggle="dropdown"
            aria-expanded="false">
            Combined actions
        </button>
        <ul class="dropdown-menu">
            <li><a class="dropdown-item" onclick="createCustom(this)">Describe your actions</a></li>
                    <li><a class="dropdown-item" onclick="createAnd(this)">And of actions</a></li>
                    <li><a class="dropdown-item" onclick="createOr(this)">Or of actions</a></li>
                    <li><a class="dropdown-item" onclick="createClick(this)">Click</a></li>
                    <li><a class="dropdown-item" onclick="createInputText(this)">InputText</a></li>
                    <li><a class="dropdown-item" onclick="createSelectList(this)">Select list</a></li>
                    <li><a class="dropdown-item" onclick="createRadioChoice(this)">Radio Button</a></li>
                    <li><a class="dropdown-item" onclick="createCheckbox(this)">Check box</a></li>
                    <li><a class="dropdown-item" onclick="createAcceptPopUp(this)">Accept popup</a></li>
                    <li><a class="dropdown-item" onclick="createCancelPopUp(this)">Cancel popup</a></li>
                    <li><a class="dropdown-item" onclick="createInputToPopUp(this)">Input to popup</a></li> 
            </ul>
    </div>
    <ol class="actions nested active">`;
        }
        let jsonArr = json.children;
        jsonArr.forEach(function(item) {
            andElement = andElement.concat(jsonToElement(item, layer + 1));
        });
        andElement = andElement.concat(footer);
        return andElement;
    }
}

function parseInput(element) {
    let s = element.parentNode.parentNode.querySelector('input').value;
    let data = {
        "expr": s
    }
    $.ajax({
        type: "POST",
        url: "/parse",
        contentType: "application/json",
        data: JSON.stringify(data),
        success: function (res) {
            console.log(res);
            let parsedElement = document.createElement("div");
            // parsedElement.innerHTML = jsonToElement(res);
            let orderedList = element.parentNode.parentNode.parentNode;

            // while (previousItem.nodeType !== 1) previousItem = previousItem.previousSibling;
            // console.log("element" + previousItem);
            res.forEach(function (action) {
                // parsedElement.innerHTML = parsedElement.innerHTML.concat(jsonToElement(action));
                // previousItem.in
                orderedList.insertAdjacentHTML('beforeend', jsonToElement(action, 1));
            });
            element.parentNode.parentNode.remove();
            console.log(parsedElement);

            element.parentNode.parentNode;

            // previousItem.insertAdjacentElement('afterend', parsedElement.innerHTML);
            console.log('hello');
        },
        error: function (xhr, status, error) {
            console.log("Error");

        }
    })
}



