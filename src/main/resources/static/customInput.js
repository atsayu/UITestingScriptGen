

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
    let clickElement = `<div class="action" type="click">
    <input type="checkbox" name="select" onclick="selecting(this)">
    Click
    <input type="text" placeholder="locator" value="${locator}">
</div>`;
    return clickElement;
}

function createFillInput(locator, text) {
    let clickElement = `<div class="action" type="input">
    <input type="checkbox" name="select" onclick="selecting(this)">
    Input
    <input type="text"  placeholder="text" value="${text}">
    to
    <input type="text" placeholder="locator" value="${locator}">
</div>`;
    return clickElement;
}

function createFillAnd(firstElement, secondElement) {
    let andElement = `<div class="action" type="and">
    <input type="checkbox" name="select" onclick="selecting(this)">
    ${firstElement}
    ${secondElement}
    <div class="btn-group dropend">
        <button type="button" class="btn btn-secondary dropdown-toggle btn-sm" data-bs-toggle="dropdown"
            aria-expanded="false">
            Add action to And
        </button>
        <ul class="dropdown-menu">
            <li><a class="dropdown-item" onclick="createAnd(this)">And</a></li>
            <li><a class="dropdown-item" onclick="createOr(this)">Or</a></li>
            <li><a class="dropdown-item" onclick="createClick(this)">Click</a></li>
            <li><a class="dropdown-item" onclick="createInputText(this)">InputText</a></li>
            </ul>
    </div>
</div>`;
    return andElement;

}

function createFillOr(firstElement, secondElement) {
    let orElement = `<div class="action" type="or">
    <input type="checkbox" name="select" onclick="selecting(this)">
    ${firstElement}
    ${secondElement}
    <div class="btn-group dropend">
        <button type="button" class="btn btn-secondary dropdown-toggle btn-sm" data-bs-toggle="dropdown"
            aria-expanded="false">
            Add action to Or
        </button>
        <ul class="dropdown-menu">
            <li><a class="dropdown-item" onclick="createAnd(this)">And</a></li>
            <li><a class="dropdown-item" onclick="createOr(this)">Or</a></li>
            <li><a class="dropdown-item" onclick="createClick(this)">Click</a></li>
            <li><a class="dropdown-item" onclick="createInputText(this)">InputText</a></li>
            </ul>
    </div>
</div>`;
    return orElement;
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

function jsonToElement(json) {
    let type = json.exprType;
    if (type === 'variable') {
        return createFromValue(json.value);
    } else if (type === 'or') {
        let orElement = `<div class="action" type="or">
    <input type="checkbox" name="select" onclick="selecting(this)">
  `;
    let footer = `<div class="btn-group dropend">
        <button type="button" class="btn btn-secondary dropdown-toggle btn-sm" data-bs-toggle="dropdown"
            aria-expanded="false">
            Add action to Or
        </button>
        <ul class="dropdown-menu">
            <li><a class="dropdown-item" onclick="createAnd(this)">And</a></li>
            <li><a class="dropdown-item" onclick="createOr(this)">Or</a></li>
            <li><a class="dropdown-item" onclick="createClick(this)">Click</a></li>
            <li><a class="dropdown-item" onclick="createInputText(this)">InputText</a></li>
            </ul>
    </div>
</div>`;

        let jsonArr = json.children;
        jsonArr.forEach(function(item) {
           orElement = orElement.concat(jsonToElement(item)) ;
        });
        orElement = orElement.concat(footer);
        return orElement;
    } else if (type === 'and') {
        let andElement = `<div class="action" type="and">
    <input type="checkbox" name="select" onclick="selecting(this)">`
  
    let footer = `<div class="btn-group dropend">
        <button type="button" class="btn btn-secondary dropdown-toggle btn-sm" data-bs-toggle="dropdown"
            aria-expanded="false">
            Add action to And
        </button>
        <ul class="dropdown-menu">
            <li><a class="dropdown-item" onclick="createAnd(this)">And</a></li>
            <li><a class="dropdown-item" onclick="createOr(this)">Or</a></li>
            <li><a class="dropdown-item" onclick="createClick(this)">Click</a></li>
            <li><a class="dropdown-item" onclick="createInputText(this)">InputText</a></li>
            </ul>
    </div>
</div>`;
        let jsonArr = json.children;
        jsonArr.forEach(function(item) {
            andElement = andElement.concat(jsonToElement(item));
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
            parsedElement.innerHTML = jsonToElement(res);
            console.log(parsedElement);
            element.parentNode.parentNode.replaceWith(parsedElement.firstElementChild);
            console.log('hello');
        },
        error: function (xhr, status, error) {
            console.log("Error");

        }
    })
}



