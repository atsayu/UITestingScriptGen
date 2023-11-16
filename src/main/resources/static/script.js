function createTestCase(element) {
    let testcaseElement = `            <div class="testcase">
    <input type="checkbox" name="select" onclick="selecting(this)">
    <span class="keyword">Scenario</span>
<input type="text" placeholder="Test case name" class="test-case-name">
    <div class="keyword">
        Do:
    </div>
    
    <div class="actions">
        <div class="btn-group dropend" id="add-action-btn">
            <button type="button" class="btn btn-secondary dropdown-toggle btn-sm" data-bs-toggle="dropdown"
                aria-expanded="false">
                Add Action
            </button>
            <ul class="dropdown-menu">
                <li><a class="dropdown-item" onclick="createAnd(this)">And</a></li>
                <li><a class="dropdown-item" onclick="createOr(this)">Or</a></li>
                <li><a class="dropdown-item" onclick="createClick(this)">Click</a></li>
                <li><a class="dropdown-item" onclick="createInputText(this)">InputText</a></li>
                <li><a class="dropdown-item" onclick="createCustom(this)">Custom</a></li>
            </ul>
        </div>
    </div>

    <div class="keyword">
        Response:
    </div>
    <div class="btn-group dropend" id="add-response-btn">
        <button type="button" class="btn btn-secondary dropdown-toggle btn-sm" data-bs-toggle="dropdown"
            aria-expanded="false">
            Add Expected Response
        </button>
        <ul class="dropdown-menu">
            <li><a class="dropdown-item" onclick="createURLResponse(this)">URL Response</a></li>
        </ul>
    </div>
    <div class="response-container">
    </div>
</div>`;
    // element.parentNode.insertAdjacentHTML('beforeend',testcaseElement);
    element.insertAdjacentHTML('beforebegin', testcaseElement);
}

function createAction(element) {

}

function createClick(element) {
    let clickElement = `<div class="action" type="click">
    <input type="checkbox" name="select" onclick="selecting(this)">
    Click
    <input type="text" placeholder="locator" value="">
</div>`;
    console.log(element.parentNode.parentNode.parentNode);
    let lastElement = element.parentNode.parentNode.parentNode;
    lastElement.insertAdjacentHTML('beforebegin', clickElement);
}

function createInputText(element) {
    let clickElement = `<div class="action" type="input">
    
    <input type="checkbox" name="select" onclick="selecting(this)">
    Input
    <input type="text"  placeholder="text">
    to
    <input type="text" placeholder="locator">
</div>`;
    console.log(element.parentNode.parentNode.parentNode);
    let lastElement = element.parentNode.parentNode.parentNode;
    lastElement.insertAdjacentHTML('beforebegin', clickElement);
}

function createCustom(element) {
    let custom = `<div class="input-group mb-3">
    <input type="text" class="form-control" placeholder="Your custom input" aria-label="Your custom input" aria-describedby="basic-addon2">
    <div class="input-group-append">
      <button class="btn btn-outline-secondary" type="button" onclick="parseInput(this)">To Action</button>
    </div>
  </div>`;
    let lastElement = element.parentNode.parentNode.parentNode;
    lastElement.insertAdjacentHTML('beforebegin', custom);
}

function createAnd(element) {
    let andElement = `<div class="action" type="and">
    <input type="checkbox" name="select" onclick="selecting(this)">
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
    let lastElement = element.parentNode.parentNode.parentNode;
    console.log(element.type);
    lastElement.insertAdjacentHTML('beforebegin', andElement);
}

function createOr(element) {
    let orElement = `<div class="action" type="or">
    <input type="checkbox" name="select" onclick="selecting(this)">
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
    let lastElement = element.parentNode.parentNode.parentNode;
    lastElement.insertAdjacentHTML('beforebegin', orElement);
}

function parseResponse(element) {
    if (element.getAttribute('type') === 'url') {
        let xml = `<Validation>      
        <type>URLValidation</type>
        <url>${element.querySelector('input').value}</url>
      </Validation>`;
        return xml;
    }

}

function parse(element) {
    // console.log(element.querySelector("input").value);
    if (element.getAttribute('type') === 'click') return `<LogicOfActions>
    <type>Click Element</type>
    <locator>${element.querySelector("[placeholder = 'locator']").value}</locator>        
    </LogicOfActions>`;
    else if (element.getAttribute('type') === 'click') {
        return `<LogicOfActions>
            <type>Input Text</type>
             <locator>${element.querySelector("input[placeholder = 'locator']").value}</locator>       
             <text>${element.querySelector("input[placeholder = 'text']").value}</text> 
            </LogicOfActions>`;
    }
    else if (element.getAttribute('type') === 'and') {
        let xml = `<LogicofActions><type>and</type>`;
        let actions = element.querySelectorAll('.action');
        for (let i = 0; i < actions.length; i++) {
            xml = xml.concat(parse(actions[i]));
        }
        xml = xml.concat(`</LogicofActions>`);
        return xml;
    } else if (element.getAttribute('type') === 'or') {
        let xml = `<LogicofActions><type>or</type>`;
        let actions = element.querySelectorAll('.action');
        for (let i = 0; i < actions.length; i++) {
            xml = xml.concat(parse(actions[i]));
        }
        xml = xml.concat(`</LogicofActions>`);
        return xml;
    }

}

function createURLResponse(element) {
    let responseElement = `<div class="response" type="url">
    <input type="checkbox" name="select" onclick="selecting(this)">
    <span>Go to</span>
    <input type="text" placeholder="expected url">
</div>`;
    let addbtn = element.parentNode.parentNode.parentNode;
    console.log(addbtn);
    addbtn.insertAdjacentHTML('beforebegin', responseElement);

}

function createTestTemplate() {
    let xml = `<TestSuite>`;
    let url = document.querySelector('#url');
    xml = xml.concat(`<url>${url.value}</url>`);
    let validTestCase = document.querySelectorAll('.valids .testcase');
    validTestCase.forEach((element) => {
        xml = xml.concat('<TestCase>');
        xml = xml.concat(`<Scenario>${element.querySelector('.test-case-name').value}</Scenario>`)
        let actions = element.querySelectorAll('.actions .action');
        actions.forEach((action) => {
            xml = xml.concat(parse(action));
        });
        let responses = element.querySelectorAll('.response');
        responses.forEach(function (response) {
            xml = xml.concat(parseResponse(response));
        });
        xml = xml.concat('</TestCase>');
    });
    xml = xml.concat('</TestSuite>');
    xml = formatXml(xml, "\t");
    console.log(xml);
}

function selecting(element) {
    //checked status === false mean before click, the check box checked status is true.
    if (element.checked === false) {
        element.checked = false;
        element.parentNode.classList.remove('selected');
        let children = element.parentNode.querySelectorAll('.action,.response');
        children.forEach(function (item) {
            let checkbox = item.querySelector("[type = 'checkbox']");
            if (checkbox.checked === true) {
                checkbox.checked = false;
                item.classList.remove('selected');
            }
        });
    } else {
        element.checked = true;
        element.parentNode.classList.add('selected');
        let children = element.parentNode.querySelectorAll('.action,.response');
        children.forEach(function (item) {
            let checkbox = item.querySelector("[type = 'checkbox']");
            if (checkbox.checked === false) {
                checkbox.checked = true;
                item.classList.add('selected');
            }
        });
    }
    
}

function formatXml(xml, tab) { // tab = optional indent value, default is tab (\t)
    var formatted = '', indent = '';
    tab = tab || '\t';
    xml.split(/>\s*</).forEach(function (node) {
        if (node.match(/^\/\w/)) indent = indent.substring(tab.length); // decrease indent by one 'tab'
        formatted += indent + '<' + node + '>\r\n';
        if (node.match(/^<?\w[^>]*[^\/]$/)) indent += tab;              // increase indent
    });
    return formatted.substring(1, formatted.length - 3);
}

function deleteSelected() {
    let selectedActions = document.querySelectorAll('.selected');
    selectedActions.forEach(function (action) {
        action.remove();
    });
}

function createClickToLogic(element) {
    let clickElement = `<li><div class="action" type="click">
    <input type="checkbox" name="select" onclick="selecting(this)">
    Click
    <input type="text" placeholder="locator" value="">
</div>
</li>`;
    console.log(element.parentNode.parentNode.parentNode.parentNode);
    let ulist = element.parentNode.parentNode.parentNode.parentNode.querySelector('.nested');
    console.log(ulist);
    // lastElement.insertAdjacentHTML('beforebegin', clickElement);
    ulist.insertAdjacentHTML('beforeend', clickElement);
}

let toggler = document.getElementsByClassName("caret");
let i = 0;

for (i = 0; i < toggler.length; i++) {
    toggler[i].addEventListener("click", function() {
        this.parentElement.querySelector(".nested").classList.toggle("active");
        this.classList.toggle("caret-down");
    });
}