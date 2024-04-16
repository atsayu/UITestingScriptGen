function createTestCase(element) {
    let testcaseElement = `
            <div class="testcase">
            <input type="checkbox" name="select" onclick="selecting(this)">
            <span class="keyword">Scenario</span>
            <input type="text" placeholder="Test case name" class="test-case-name">
            <div class="btn-group dropend root-btn" id="add-action-btn">
                <button type="button" class="btn btn-secondary dropdown-toggle btn-sm" data-bs-auto-close="outside" data-bs-toggle="dropdown"
                        aria-expanded="false">
                    Add Action
                </button>
                <ul class="dropdown-menu">
                <li><a class="dropdown-item" onclick="createCustom(this)">Actions flow</a></li>
                <li class="dropend">
                    <a class="dropdown-item dropdown-toggle" data-bs-toggle="dropdown">Normal Action</a>
                    <ul class="dropdown-menu">
                        <li><a class="dropdown-item" onclick="createOuterAnd(this.parentNode.parentNode)">And of actions</a></li>
                    <li><a class="dropdown-item" onclick="createOuterOr(this.parentNode.parentNode)">Or of actions</a></li>
                    <li><a class="dropdown-item" onclick="addClickElement(this.parentNode.parentNode)">Click</a></li>
                    <li><a class="dropdown-item" onclick="addInputTextElement(this.parentNode.parentNode)">InputText</a></li>
                    <li><a class="dropdown-item" onclick="addSelectList(this.parentNode.parentNode)">Select list</a></li>
                    <li><a class="dropdown-item" onclick="addRadioAction(this.parentNode.parentNode)">Radio Button</a></li>
                    <li><a class="dropdown-item" onclick="addCheckbox(this.parentNode.parentNode)">Check box</a></li>
                    <li><a class="dropdown-item" onclick="addAcceptPopup(this.parentNode.parentNode)">Accept popup</a></li>
                    <li><a class="dropdown-item" onclick="addCancelPopUp(this.parentNode.parentNode)">Cancel popup</a></li>
                    <li><a class="dropdown-item" onclick="addInputPopup(this.parentNode.parentNode)">Input to popup</a></li>
                    </ul>
                </li>
                <li class="dropend">
                    <a class="dropdown-item dropdown-toggle" data-bs-toggle="dropdown">Assertion</a>
                    <ul class="dropdown-menu">
                                             <li><a class="dropdown-item" onclick="addURLAssertElement(this.parentNode.parentNode)">URL Verification</a></li>
                     <li><a class="dropdown-item" onclick="addTextAssert(this.parentNode.parentNode)">Text Verification</a></li>
                     <li><a class="dropdown-item" onclick="addElementTextAssert(this.parentNode.parentNode)">Element Text Assert</a></li>
                    <li><a class="dropdown-item" onclick="addAssertVisibleElement(this.parentNode.parentNode)">Visible Element assert</a></li>
                      
                    </ul>
                </li> 
<!--                    <li><a class="dropdown-item" onclick="createOuterAnd(this)">And of actions</a></li>-->
                </ul>
            </div>
            <div class="keyword">
                Do:
            </div>


            <ol class="actions outter">

            </ol>

<!--            <div class="keyword">-->
<!--                Response:-->
<!--            </div>-->
<!--            <ol class="responses">-->

<!--            </ol>-->
<!--            <div class="btn-group dropend" id="add-response-btn">-->
<!--                <button type="button" class="btn btn-secondary dropdown-toggle btn-sm" data-bs-toggle="dropdown"-->
<!--                        aria-expanded="false">-->
<!--                    Add Expected Response-->
<!--                </button>-->
<!--                <ul class="dropdown-menu">-->
<!--                    <li><a class="dropdown-item" onclick="createURLResponse(this)">URL Response</a></li>-->
<!--                </ul>-->
<!--            </div>-->
<!--            <div class="response-container">-->
<!--            </div>-->
                   <div>
                    <button class="btn btn-danger btn-sm delete-action-btn d-none" onclick="deleteSelectedAction(this)">
                        Delete Selected Element
                        <i class="fa-solid fa-trash"></i>
                    </button>
            </div>
        </div>`;
    // element.parentNode.insertAdjacentHTML('beforeend',testcaseElement);
    element.insertAdjacentHTML('beforebegin', testcaseElement);
}

function addClickElement(element) {
    let clickElement = createClick();
    let actionList = element.parentNode.parentNode.parentNode.parentNode.querySelector('.actions');
    console.log(actionList);
    // lastElement.insertAdjacentHTML('beforebegin', clickElement);
    // ulist.insertAdjacentHTML('beforeend', clickElement);
    actionList.insertAdjacentElement('beforeend', clickElement);
}

function  createClick(locator) {
//     let clickElement = `<div class="action" type="click">
//     <input type="checkbox" name="select" onclick="selecting(this)">
//     Click
//     <input type="text" placeholder="locator" value="5">
// </div>`;
//     console.log(element.parentNode.parentNode.parentNode);
//     let lastElement = element.parentNode.parentNode.parentNode;
//     lastElement.insertAdjacentHTML('beforebegin', clickElement);

//     let clickElement = `<li><div class="action" type="click">
//     <input type="checkbox" name="select" onclick="selecting(this)">
//     Click
//     <input type="text" placeholder="locator" value="">
// </div>
// </li>`;
    let clickElement =  `<li>
                    <div class="action" type="click">
                        <div class="label-button">
                            <input type="checkbox" name="select" onclick="selecting(this.parentNode.parentNode)">
                            <button type="button" class="up">${(locator && locator.length <= 3)? 'Click ' + locator : 'Click ...'}</button>
                        </div>
                        <span class="info nested">${locator? 'Click ' +locator : '...'}</span>
                        <div class="user-input nested">
                            Click <input type="text" placeholder="locator" value="${locator? locator : ''}">
                            <button type="button">Update</button>
                        </div>
                    </div>
                </li>`;
    let template = document.createElement('template');
    template.innerHTML = clickElement;

    let label_btn = template.content.querySelector('.user-input button[type = "button"]');



    //Update from user input function
    template.content.querySelector('.user-input button[type = "button"]').addEventListener('click', function () {
        console.log(this);
        let label_btn = this.parentNode.parentNode.querySelector('.label-button').querySelector('button[type="button"]');
        console.log(label_btn);
        let locator = this.parentNode.querySelector('input[type="text"]').value;
        let info = this.parentNode.parentNode.querySelector('span.info');
        info.textContent = 'Click ' + locator;
        if (locator.length > 3) {
            label_btn.innerHTML = 'Click ' + locator.substring(0, 3) + '...';
        } else {
            label_btn.innerHTML = 'Click '+ locator;
        }
    });

    template.content.querySelector('.user-input input[type = "text"]').addEventListener('keydown', function(event) {
        // event.preventDefault()
        console.log('ok')
        if (event.keyCode === 13) {
            template.content.querySelector('.user-input button[type = "button"]').click();
        }
    });




    //Show and hide user input
    let showHideBtn = template.content.querySelector('.label-button button[type = "button"]');
    showHideBtn.addEventListener('click', function () {
        this.classList.toggle('down');
        let user_input = this.parentNode.parentNode.querySelector('.user-input');
        user_input.classList.toggle('nested');
    });

    showHideBtn.addEventListener('mouseover', function() {
        let info = this.parentNode.parentNode.querySelector('span.info');
        if (info.classList.contains('nested')) info.classList.remove('nested');
    });

    showHideBtn.addEventListener('mouseout', function () {
        let info = this.parentNode.parentNode.querySelector('span.info');
        if (!info.classList.contains('nested')) info.classList.add('nested');
    });





    return template.content.firstElementChild;
}
function addSelectList(element) {
    let selectListElement = createSelectList();
    console.log(element.parentNode.parentNode.parentNode.parentNode);
    let list = element.parentNode.parentNode.parentNode.parentNode.querySelector('.actions');
    list.insertAdjacentElement('beforeend', selectListElement);
}

function createSelectList(listName) {
    let selectListElement = `<li>
                    <div class="action" type="select-list">
                        <div class="label-button">
                            <input type="checkbox" name="select" onclick="selecting(this.parentNode)">
                            <button type="button" class="up">Select ...   </button>
                        </div>
                        <span class="info nested">Select ... for ...</span>
                        <div class="user-input nested">
                            Select <input type="text" placeholder="item" value="${listName ? listName: ''}"> for <input type="text" placeholder="list" value="${listName? listName : ''}">
                            <button type="button">Update</button>
                        </div>
                    </div>
                </li>`;

    let template = document.createElement('template');
    template.innerHTML = selectListElement;

    let label_btn = template.content.querySelector('.user-input button[type = "button"]');

    //Update from user input function
    template.content.querySelector('.user-input button[type = "button"]').addEventListener('click', function () {
        console.log(this);
        let label_btn = this.parentNode.parentNode.querySelector('.label-button').querySelector('button[type="button"]');
        console.log(label_btn);
        let list = this.parentNode.querySelector('input[type="text"][placeholder="list"]').value;
        let item = this.parentNode.querySelector('input[type="text"][placeholder="item"]').value;
        let info = this.parentNode.parentNode.querySelector('span.info');
        info.textContent = 'Select ' + item + ' for ' + list;
        if (item.length > 3) {
            label_btn.innerHTML = 'Input ' + item.substring(0, 3) + '...';
        } else {
            label_btn.innerHTML = 'Input '+ item;
        }
    });

    //Show and hide user input
    let showHideBtn = template.content.querySelector('.label-button button[type = "button"]');
    showHideBtn.addEventListener('click', function () {
        this.classList.toggle('down');
        let user_input = this.parentNode.parentNode.querySelector('.user-input');
        user_input.classList.toggle('nested');
    });


    showHideBtn.addEventListener('mouseover', function() {
        let info = this.parentNode.parentNode.querySelector('span.info');
        if (info.classList.contains('nested')) info.classList.remove('nested');
    });

    showHideBtn.addEventListener('mouseout', function () {
        let info = this.parentNode.parentNode.querySelector('span.info');
        if (!info.classList.contains('nested')) info.classList.add('nested');
    });

    return template.content.firstElementChild;

}
function addRadioAction(element) {
    let radioElement = createRadioAction();

    let list = element.parentNode.parentNode.parentNode.parentNode.querySelector('.actions');
    list.insertAdjacentElement('beforeend', radioElement);
}

function createRadioAction(groupName) {
    let inputElement = `<li>
                    <div class="action" type="radio">
                        <div class="label-button">
                            <input type="checkbox" name="select" onclick="selecting(this)">
                            <button type="button" class="up">Choose ...   </button>
                        </div>
                        <span class="info nested">Choose ... for ...</span>
                        <div class="user-input nested">
                            Choose <input type="text" placeholder="choice" value="${groupName? groupName+'_choice' : ''}"> for <input type="text" placeholder="question" value="${groupName? groupName : ''}">
                            <button type="button">Update</button>
                        </div>
                    </div>
                </li>`;

    let template = document.createElement('template');
    template.innerHTML = inputElement;

    //Update from user input function
    template.content.querySelector('.user-input button[type = "button"]').addEventListener('click', function () {
        console.log(this);
        let label_btn = this.parentNode.parentNode.querySelector('.label-button').querySelector('button[type="button"]');
        console.log(label_btn);
        let question = this.parentNode.querySelector('input[type="text"][placeholder="question"]').value;
        let choice = this.parentNode.querySelector('input[type="text"][placeholder="choice"]').value;
        let info = this.parentNode.parentNode.querySelector('span.info');
        info.textContent = 'Choose ' + choice + ' for ' + question;
        if (choice.length > 3) {
            label_btn.innerHTML = 'Choose ' + choice.substring(0, 3) + '...';
        } else {
            label_btn.innerHTML = 'Choose '+ choice;
        }
    });


    //Show and hide user input
    let showHideBtn = template.content.querySelector('.label-button button[type = "button"]');
    showHideBtn.addEventListener('click', function () {
        this.classList.toggle('down');
        let user_input = this.parentNode.parentNode.querySelector('.user-input');
        user_input.classList.toggle('nested');
    });


    showHideBtn.addEventListener('mouseover', function() {
        let info = this.parentNode.parentNode.querySelector('span.info');
        if (info.classList.contains('nested')) info.classList.remove('nested');
    });

    showHideBtn.addEventListener('mouseout', function () {
        let info = this.parentNode.parentNode.querySelector('span.info');
        if (!info.classList.contains('nested')) info.classList.add('nested');
    });



    return template.content.firstElementChild;
}

function addCheckbox(element) {
    let checkboxElement = createCheckbox();
    console.log(element.parentNode.parentNode.parentNode.parentNode);
    let list = element.parentNode.parentNode.parentNode.parentNode.querySelector('.actions');
    list.insertAdjacentElement('beforeend', checkboxElement);
}
function createCheckbox(question) {
    let checkboxElement = `<li>
                    <div class="action" type="checkbox">
                        <div class="label-button">
                            <input type="checkbox" name="select" onclick="selecting(this)">
                            <button type="button" class="up">Select ...   </button>
                        </div>
                        <span class="info nested">Select ... for ...</span>
                        <div class="user-input nested">
                            Select <input type="text" placeholder="answer" value="${question? question + '_answer' : ''}"> for <input type="text" placeholder="question" value="${question? question : ''}">
                            <button type="button">Update</button>
                        </div>
                    </div>
                </li>`;

    let template = document.createElement('template');
    template.innerHTML = checkboxElement;

    //Update from user input function
    template.content.querySelector('.user-input button[type = "button"]').addEventListener('click', function () {
        console.log(this);
        let label_btn = this.parentNode.parentNode.querySelector('.label-button').querySelector('button[type="button"]');
        console.log(label_btn);
        let question = this.parentNode.querySelector('input[type="text"][placeholder="question"]').value;
        let label = this.parentNode.querySelector('input[type="text"][placeholder="label"]').value;
        let info = this.parentNode.parentNode.querySelector('span.info');
        info.textContent = 'Select ' + label + ' for ' + question;
        if (label.length > 3) {
            label_btn.innerHTML = 'Select ' + label.substring(0, 3) + '...';
        } else {
            label_btn.innerHTML = 'Select '+ label;
        }
    });


    //Show and hide user input
    let showHideBtn = template.content.querySelector('.label-button button[type = "button"]');
    showHideBtn.addEventListener('click', function () {
        this.classList.toggle('down');
        let user_input = this.parentNode.parentNode.querySelector('.user-input');
        user_input.classList.toggle('nested');
    });


    showHideBtn.addEventListener('mouseover', function() {
        let info = this.parentNode.parentNode.querySelector('span.info');
        if (info.classList.contains('nested')) info.classList.remove('nested');
    });

    showHideBtn.addEventListener('mouseout', function () {
        let info = this.parentNode.parentNode.querySelector('span.info');
        if (!info.classList.contains('nested')) info.classList.add('nested');
    });


    return template.content.firstElementChild;
}

function addAcceptPopup(element) {
    let acceptPopupElement = createAcceptPopUp();
    console.log(element.parentNode.parentNode.parentNode.parentNode);
    let list = element.parentNode.parentNode.parentNode.parentNode.querySelector('.actions');
    list.insertAdjacentElement('beforeend', acceptPopupElement);
}

function createAcceptPopUp() {
    let accept = `<li>
                    <div class="action" type="accept-popup">
                    <span class="label-button">
                            <input type="checkbox" name="select" onclick="selecting(this)">
                        </span>
                        Accept alert
                    </div>
                </li>`;
    let template = document.createElement('template');
    template.innerHTML = accept;


    return template.content.firstElementChild;
}

function addCancelPopUp(element) {
    let cancelPopUpElement = createCancelPopUp();
    console.log(element.parentNode.parentNode.parentNode.parentNode);
    let list = element.parentNode.parentNode.parentNode.parentNode.querySelector('.actions');
    list.insertAdjacentElement('beforeend', cancelPopUpElement);
}
function createCancelPopUp(element) {
    let cancel = `<li>
                    <div class="action" type="cancel-popup">
                    <span class="label-button">
                            <input type="checkbox" name="select" onclick="selecting(this)">
                        </span>
                        Cancel alert
                    </div>
                </li>`;
    let template = document.createElement('template');
    template.innerHTML = cancel;

    return template.content.firstElementChild;
}

function addInputPopup(element) {
    let inputPopupElement = createInputToPopUp();
    let list = element.parentNode.parentNode.parentNode.parentNode.querySelector('.actions');
    list.insertAdjacentElement('beforeend', inputPopupElement);
}
function createInputToPopUp(value) {
    let inputElement = `<li>
                    <div class="action" type="input-popup">
                        <div class="label-button">
                            <input type="checkbox" name="select" onclick="selecting(this)">
                            <button type="button" class="up">Input popup ... </button>
                        </div>
                        <span class="info nested">Input popup ...</span>
                        <div class="user-input nested">
                            Input <input type="text" placeholder="text" value="${value? value : ''}"> to popup
                            <button type="button">Update</button>
                        </div>
                    </div>
                </li>`;

    let template = document.createElement('template');
    template.innerHTML = inputElement;

    //Update from user input function
    template.content.querySelector('.user-input button[type = "button"]').addEventListener('click', function () {
        console.log(this);
        let label_btn = this.parentNode.parentNode.querySelector('.label-button').querySelector('button[type="button"]');
        console.log(label_btn);
        let text = this.parentNode.querySelector('input[type="text"][placeholder="text"]').value;
        let info = this.parentNode.parentNode.querySelector('span.info');
        info.textContent = 'Input ' + text + 'to popup';
        if (text.length > 3) {
            label_btn.innerHTML = 'Input popup...';
        } else {
            label_btn.innerHTML = 'Input popup...';
        }
    });


    //Show and hide user input
    let showHideBtn = template.content.querySelector('.label-button button[type = "button"]');
    showHideBtn.addEventListener('click', function () {
        this.classList.toggle('down');
        let user_input = this.parentNode.parentNode.querySelector('.user-input');
        user_input.classList.toggle('nested');
    });


    showHideBtn.addEventListener('mouseover', function() {
        let info = this.parentNode.parentNode.querySelector('span.info');
        if (info.classList.contains('nested')) info.classList.remove('nested');
    });

    showHideBtn.addEventListener('mouseout', function () {
        let info = this.parentNode.parentNode.querySelector('span.info');
        if (!info.classList.contains('nested')) info.classList.add('nested');
    });


    return template.content.firstElementChild;
}

function addInputTextElement(element) {
    let inputElement = createInputText();
    console.log(element.parentNode.parentNode.parentNode.parentNode);
    let list = element.parentNode.parentNode.parentNode.parentNode.querySelector('.actions');
    list.insertAdjacentElement('beforeend', inputElement);
}

function createInputText(locator) {
    let inputElement = `<li>
                    <div class="action" type="input-text">
                        <div class="label-button">
                            <input type="checkbox" name="select" onclick="selecting(this.parentNode)">
                            <button type="button" class="up">Input ...  </button>
                        </div>
                        <span class="info nested">${locator? 'Input valid_' +locator + ' to ' +locator : '...'}</span>
                        <div class="user-input nested">
                            Input <input type="text" placeholder="text" value="${locator? 'valid_'+locator : ''}"> to <input type="text" placeholder="locator" value="${locator ? locator : ''}">
                            <button type="button">Update</button>
                        </div>
                    </div>
                </li>`;

    let template = document.createElement('template');
    template.innerHTML = inputElement;

    //Update from user input function
    template.content.querySelector('.user-input button[type = "button"]').addEventListener('click', function () {
        console.log(this);
        let label_btn = this.parentNode.parentNode.querySelector('.label-button').querySelector('button[type="button"]');
        console.log(label_btn);
        let locator = this.parentNode.querySelector('input[type="text"][placeholder="locator"]').value;
        let text = this.parentNode.querySelector('input[type="text"][placeholder="text"]').value;
        let info = this.parentNode.parentNode.querySelector('span.info');
        info.textContent = 'Input ' + text + ' to ' + locator;
        if (text.length > 3) {
            label_btn.innerHTML = 'Input ' + locator.substring(0, 3) + '...';
        } else {
            label_btn.innerHTML = 'Input '+ locator;
        }
    });


    //Show and hide user input
    let showHideBtn = template.content.querySelector('.label-button button[type = "button"]');
    showHideBtn.addEventListener('click', function () {
        this.classList.toggle('down');
        let user_input = this.parentNode.parentNode.querySelector('.user-input');
        user_input.classList.toggle('nested');
    });


    showHideBtn.addEventListener('mouseover', function() {
        let info = this.parentNode.parentNode.querySelector('span.info');
        if (info.classList.contains('nested')) info.classList.remove('nested');
    });

    showHideBtn.addEventListener('mouseout', function () {
        let info = this.parentNode.parentNode.querySelector('span.info');
        if (!info.classList.contains('nested')) info.classList.add('nested');
    });



    return template.content.firstElementChild;
}

function createCustom(element) {
    let custom = `<li>
            
            <div class="action" type="custom">
            <input type="checkbox" name="select" onclick="selecting(this)">
             <div class="custom-container" style="display: inline-block; width: 90%">
                 
                <input type="text" class="custom-intput-field"  style="width: 80%" placeholder="Your custom action">
                <button class="" style="margin-top: 5px; margin-bottom: 5px" type="button" id="" onclick="parseInput(this.parentNode)">Create Action</button>
            </div>
</div>
        
        
    </li>`;


    let list = element.parentNode.parentNode.parentNode.parentNode.querySelector('.actions');
    list.insertAdjacentHTML('beforeend', custom);
}

function createOuterAnd(element) {
    let andElement = `<li>
<div class = "action" type="and">

    <input type="checkbox" name="select" onclick="selecting(this)">
    <span class="info nested"></span>
    <div class="btn-group dropend root-btn" id="add-action-btn">
                <button type="button" class="btn btn-secondary dropdown-toggle btn-sm" data-bs-auto-close="outside" data-bs-toggle="dropdown"
                        aria-expanded="false">
                    Combined Action
                </button>
                <ul class="dropdown-menu">
                <li><a class="dropdown-item" onclick="createCustom(this)">Actions flow</a></li>
                <li class="dropend">
                    <a class="dropdown-item dropdown-toggle" data-bs-toggle="dropdown">Normal Action</a>
                    <ul class="dropdown-menu">
                        <li><a class="dropdown-item" onclick="createAnd(this.parentNode.parentNode)">And of actions</a></li>
                    <li><a class="dropdown-item" onclick="createOr(this.parentNode.parentNode)">Or of actions</a></li>
                    <li><a class="dropdown-item" onclick="addClickElement(this.parentNode.parentNode)">Click</a></li>
                    <li><a class="dropdown-item" onclick="addInputTextElement(this.parentNode.parentNode)">InputText</a></li>
                    <li><a class="dropdown-item" onclick="addSelectList(this.parentNode.parentNode)">Select list</a></li>
                    <li><a class="dropdown-item" onclick="addRadioAction(this.parentNode.parentNode)">Radio Button</a></li>
                    <li><a class="dropdown-item" onclick="addCheckbox(this.parentNode.parentNode)">Check box</a></li>
                    <li><a class="dropdown-item" onclick="addAcceptPopup(this.parentNode.parentNode)">Accept popup</a></li>
                    <li><a class="dropdown-item" onclick="addCancelPopUp(this.parentNode.parentNode)">Cancel popup</a></li>
                    <li><a class="dropdown-item" onclick="addInputPopup(this.parentNode.parentNode)">Input to popup</a></li>
                    </ul>
                </li>
                <li class="dropend">
                    <a class="dropdown-item dropdown-toggle" data-bs-toggle="dropdown">Assertion</a>
                    <ul class="dropdown-menu">
                                             <li><a class="dropdown-item" onclick="addURLAssertElement(this.parentNode.parentNode)">URL Verification</a></li>
                     <li><a class="dropdown-item" onclick="addTextAssert(this.parentNode.parentNode)">Text Verification</a></li>
                     <li><a class="dropdown-item" onclick="addElementTextAssert(this.parentNode.parentNode)">Element Text Assert</a></li>
                    <li><a class="dropdown-item" onclick="addAssertVisibleElement(this.parentNode.parentNode)">Visible Element assert</a></li>
                      
                    </ul>
                </li> 
<!--                    <li><a class="dropdown-item" onclick="createOuterAnd(this)">And of actions</a></li>-->
                </ul>
            </div>
    <ol class="actions nested active">

                </ol>
</div>
</li>
`;
    let list = element.parentNode.parentNode.parentNode.parentNode.querySelector('.actions');
    console.log(element.type);
    list.insertAdjacentHTML('beforeend', andElement);
}
function createAnd(element) {
    let andElement = `<li><div class = "action" type="and">
    
    <input type="checkbox" name="select" onclick="selecting(this)">
    <span class="caret" onclick="showAndHide(this)"></span>
    <div class="btn-group dropend root-btn" id="add-action-btn">
                <button type="button" class="btn btn-secondary dropdown-toggle btn-sm" data-bs-auto-close="outside" data-bs-toggle="dropdown"
                        aria-expanded="false">
                    Combined Action
                </button>
                <ul class="dropdown-menu">
                <li><a class="dropdown-item" onclick="createCustom(this)">Actions flow</a></li>
                <li class="dropend">
                    <a class="dropdown-item dropdown-toggle" data-bs-toggle="dropdown">Normal Action</a>
                    <ul class="dropdown-menu">
                        <li><a class="dropdown-item" onclick="createAnd(this.parentNode.parentNode)">And of actions</a></li>
                    <li><a class="dropdown-item" onclick="createOr(this.parentNode.parentNode)">Or of actions</a></li>
                    <li><a class="dropdown-item" onclick="addClickElement(this.parentNode.parentNode)">Click</a></li>
                    <li><a class="dropdown-item" onclick="addInputTextElement(this.parentNode.parentNode)">InputText</a></li>
                    <li><a class="dropdown-item" onclick="addSelectList(this.parentNode.parentNode)">Select list</a></li>
                    <li><a class="dropdown-item" onclick="addRadioAction(this.parentNode.parentNode)">Radio Button</a></li>
                    <li><a class="dropdown-item" onclick="addCheckbox(this.parentNode.parentNode)">Check box</a></li>
                    <li><a class="dropdown-item" onclick="addAcceptPopup(this.parentNode.parentNode)">Accept popup</a></li>
                    <li><a class="dropdown-item" onclick="addCancelPopUp(this.parentNode.parentNode)">Cancel popup</a></li>
                    <li><a class="dropdown-item" onclick="addInputPopup(this.parentNode.parentNode)">Input to popup</a></li>
                    </ul>
                </li>
                <li class="dropend">
                    <a class="dropdown-item dropdown-toggle" data-bs-toggle="dropdown">Assertion</a>
                    <ul class="dropdown-menu">
                                             <li><a class="dropdown-item" onclick="addURLAssertElement(this.parentNode.parentNode)">URL Verification</a></li>
                     <li><a class="dropdown-item" onclick="addTextAssert(this.parentNode.parentNode)">Text Verification</a></li>
                     <li><a class="dropdown-item" onclick="addElementTextAssert(this.parentNode.parentNode)">Element Text Assert</a></li>
                    <li><a class="dropdown-item" onclick="addAssertVisibleElement(this.parentNode.parentNode)">Visible Element assert</a></li>
                      
                    </ul>
                </li> 
<!--                    <li><a class="dropdown-item" onclick="createOuterAnd(this)">And of actions</a></li>-->
                </ul>
            </div>
    <ol class="actions nested">

                </ol>
</div>
</li>
`;
    let list = element.parentNode.parentNode.parentNode.parentNode.querySelector('.actions');
    console.log(element.type);
    list.insertAdjacentHTML('beforeend', andElement);
}
function createOuterOr(element) {
    let orElement = `<li><div class = "action" type="or">

    <input type="checkbox" name="select" onclick="selecting(this)">
    <div class="btn-group dropend root-btn" id="add-action-btn">
                <button type="button" class="btn btn-secondary dropdown-toggle btn-sm" data-bs-auto-close="outside" data-bs-toggle="dropdown"
                        aria-expanded="false">
                    Combined Action
                </button>
                <ul class="dropdown-menu">
                <li><a class="dropdown-item" onclick="createCustom(this)">Actions flow</a></li>
                <li class="dropend">
                    <a class="dropdown-item dropdown-toggle" data-bs-toggle="dropdown">Normal Action</a>
                    <ul class="dropdown-menu">
                        <li><a class="dropdown-item" onclick="createAnd(this.parentNode.parentNode)">And of actions</a></li>
                    <li><a class="dropdown-item" onclick="createOr(this.parentNode.parentNode)">Or of actions</a></li>
                    <li><a class="dropdown-item" onclick="addClickElement(this.parentNode.parentNode)">Click</a></li>
                    <li><a class="dropdown-item" onclick="addInputTextElement(this.parentNode.parentNode)">InputText</a></li>
                    <li><a class="dropdown-item" onclick="addSelectList(this.parentNode.parentNode)">Select list</a></li>
                    <li><a class="dropdown-item" onclick="addRadioAction(this.parentNode.parentNode)">Radio Button</a></li>
                    <li><a class="dropdown-item" onclick="addCheckbox(this.parentNode.parentNode)">Check box</a></li>
                    <li><a class="dropdown-item" onclick="addAcceptPopup(this.parentNode.parentNode)">Accept popup</a></li>
                    <li><a class="dropdown-item" onclick="addCancelPopUp(this.parentNode.parentNode)">Cancel popup</a></li>
                    <li><a class="dropdown-item" onclick="addInputPopup(this.parentNode.parentNode)">Input to popup</a></li>
                    </ul>
                </li>
                <li class="dropend">
                    <a class="dropdown-item dropdown-toggle" data-bs-toggle="dropdown">Assertion</a>
                    <ul class="dropdown-menu">
                                             <li><a class="dropdown-item" onclick="addURLAssertElement(this.parentNode.parentNode)">URL Verification</a></li>
                     <li><a class="dropdown-item" onclick="addTextAssert(this.parentNode.parentNode)">Text Verification</a></li>
                     <li><a class="dropdown-item" onclick="addElementTextAssert(this.parentNode.parentNode)">Element Text Assert</a></li>
                    <li><a class="dropdown-item" onclick="addAssertVisibleElement(this.parentNode.parentNode)">Visible Element assert</a></li>
                      
                    </ul>
                </li> 
<!--                    <li><a class="dropdown-item" onclick="createOuterAnd(this)">And of actions</a></li>-->
                </ul>
            </div>
    <ol class="actions nested active">

                </ol>
</div>
</li>
`;
    let list = element.parentNode.parentNode.parentNode.parentNode.querySelector('.actions');
    list.insertAdjacentHTML('beforeend', orElement);
}
function createOr(element) {
    let orElement = `<li><div class = "action" type="or">

    <input type="checkbox" name="select" onclick="selecting(this)">
    <span class="caret" onclick="showAndHide(this)"></span>
    <div class="btn-group dropend root-btn" id="add-action-btn">
                <button type="button" class="btn btn-secondary dropdown-toggle btn-sm" data-bs-auto-close="outside" data-bs-toggle="dropdown"
                        aria-expanded="false">
                    Combined Action
                </button>
                <ul class="dropdown-menu">
                <li><a class="dropdown-item" onclick="createCustom(this)">Actions flow</a></li>
                <li class="dropend">
                    <a class="dropdown-item dropdown-toggle" data-bs-toggle="dropdown">Normal Action</a>
                    <ul class="dropdown-menu">
                        <li><a class="dropdown-item" onclick="createAnd(this.parentNode.parentNode)">And of actions</a></li>
                    <li><a class="dropdown-item" onclick="createOr(this.parentNode.parentNode)">Or of actions</a></li>
                    <li><a class="dropdown-item" onclick="addClickElement(this.parentNode.parentNode)">Click</a></li>
                    <li><a class="dropdown-item" onclick="addInputTextElement(this.parentNode.parentNode)">InputText</a></li>
                    <li><a class="dropdown-item" onclick="addSelectList(this.parentNode.parentNode)">Select list</a></li>
                    <li><a class="dropdown-item" onclick="addRadioAction(this.parentNode.parentNode)">Radio Button</a></li>
                    <li><a class="dropdown-item" onclick="addCheckbox(this.parentNode.parentNode)">Check box</a></li>
                    <li><a class="dropdown-item" onclick="addAcceptPopup(this.parentNode.parentNode)">Accept popup</a></li>
                    <li><a class="dropdown-item" onclick="addCancelPopUp(this.parentNode.parentNode)">Cancel popup</a></li>
                    <li><a class="dropdown-item" onclick="addInputPopup(this.parentNode.parentNode)">Input to popup</a></li>
                    </ul>
                </li>
                <li class="dropend">
                    <a class="dropdown-item dropdown-toggle" data-bs-toggle="dropdown">Assertion</a>
                    <ul class="dropdown-menu">
                                             <li><a class="dropdown-item" onclick="addURLAssertElement(this.parentNode.parentNode)">URL Verification</a></li>
                     <li><a class="dropdown-item" onclick="addTextAssert(this.parentNode.parentNode)">Text Verification</a></li>
                     <li><a class="dropdown-item" onclick="addElementTextAssert(this.parentNode.parentNode)">Element Text Assert</a></li>
                    <li><a class="dropdown-item" onclick="addAssertVisibleElement(this.parentNode.parentNode)">Visible Element assert</a></li>
                      
                    </ul>
                </li> 
<!--                    <li><a class="dropdown-item" onclick="createOuterAnd(this)">And of actions</a></li>-->
                </ul>
            </div>
    <ol class="actions nested">

                </ol>
</div>
</li>
`;
    let list = element.parentNode.parentNode.parentNode.parentNode.querySelector('.actions');
    list.insertAdjacentHTML('beforeend', orElement);
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
    if (element.getAttribute('type') === 'click') return `<LogicExpressionOfActions>
    <type>Click Element</type>
    <locator>${element.querySelector("[placeholder = 'locator']").value}</locator>        
    </LogicExpressionOfActions>`;
    else if (element.getAttribute('type') === 'input-text') {
        return `<LogicExpressionOfActions>
            <type>Input Text</type>
             <locator>${element.querySelector("input[placeholder = 'locator']").value}</locator>       
             <text>${element.querySelector("input[placeholder = 'text']").value}</text> 
            </LogicExpressionOfActions>`;
    } else if (element.getAttribute('type') === 'input-popup'){
        return `<LogicExpressionOfActions>
            <type>Input to popup</type>     
             <text>${element.querySelector("input[placeholder = 'text']").value}</text> 
            </LogicExpressionOfActions>`;
    } else if (element.getAttribute('type') === 'accept-popup') {
        return `<LogicExpressionOfActions>
            <type>Accept popup</type>     
            </LogicExpressionOfActions>`;
    } else if (element.getAttribute('type') === 'cancel-popup') {
            return `<LogicExpressionOfActions>
            <type>Cancel popup</type>     
            </LogicExpressionOfActions>`;
    }
    else if (element.getAttribute('type') === 'select-list') {
        return `<LogicExpressionOfActions>
            <type>Select List</type>
             <list>${element.querySelector("input[placeholder = 'list']").value}</list>       
             <value>${element.querySelector("input[placeholder = 'item']").value}</value> 
            </LogicExpressionOfActions>`;
    } else if (element.getAttribute('type') === 'checkbox') {
        return ` <LogicExpressionOfActions>
                   <type>Select Checkbox</type>
                   <question>${element.querySelector("input[placeholder = 'question']").value}</question>
                   <answer>${element.querySelector("input[placeholder = 'answer']").value}</answer>
                       </LogicExpressionOfActions>`;
    }
    else if (element.getAttribute('type') === 'radio') {
        return `<LogicExpressionOfActions>
            <type>Select Radio Button</type>
             <choice>${element.querySelector("input[placeholder = 'choice']").value}</choice>       
             <question>${element.querySelector("input[placeholder = 'question']").value}</question> 
            </LogicExpressionOfActions>`;
    } else if (element.getAttribute('type') === 'verify-url') {
        return `<LogicExpressionOfActions>
            <type>Verify URL</type>
             <url>${element.querySelector("input[placeholder = 'url']").value}</url>       
            </LogicExpressionOfActions>`;
    } else if (element.getAttribute('type') === 'verify-text') {
        return `<LogicExpressionOfActions>
            <type>Verify Text</type>
             <url>${element.querySelector("input[placeholder = 'text']").value}</url>       
            </LogicExpressionOfActions>`;
    } else if (element.getAttribute('type') === 'verify-element') {
        return `<LogicExpressionOfActions>
            <type>Page Element Assertion</type>
             <url>${element.querySelector("input[placeholder = 'element']").value}</url>       
            </LogicExpressionOfActions>`;
    }
    else if (element.getAttribute('type') === 'and') {
        let xml = `<LogicExpressionOfActions><type>and</type>`;
        let actions = element.querySelector('.actions');
        console.log(actions.children);
        Array.from(actions.children).forEach(child => {
           xml = xml.concat(parse(child.firstElementChild));
        });
        // for (let i = 0; i < actions.length; i++) {
        //     xml = xml.concat(parse(actions[i]));
        // }
        xml = xml.concat(`</LogicExpressionOfActions>`);
        return xml;
    } else if (element.getAttribute('type') === 'or') {
        let xml = `<LogicExpressionOfActions><type>or</type>`;
        let actions = element.querySelector('.actions');
        Array.from(actions.children).forEach(child => {
            console.log(child.firstElementChild);
            xml = xml.concat(parse(child.firstElementChild));
        });
        // for (let i = 0; i < actions.length; i++) {
        //     xml = xml.concat(parse(actions[i]));
        // }
        xml = xml.concat(`</LogicExpressionOfActions>`);
        return xml;
    }

}

function addURLAssertElement(element) {
    let assertURLElement = createURLAssertElement();
    let actionList = element.parentNode.parentNode.parentNode.parentNode.querySelector('.actions');
    actionList.insertAdjacentElement('beforeend', assertURLElement);
}

function createURLAssertElement(url) {
    let clickElement =  `<li>
                    <div class="action" type="verify-url">
                        <div class="label-button">
                            <input type="checkbox" name="select" onclick="selecting(this.parentNode)">
                            <button type="button" class="up">Asser URL ...   </button>
                        </div>
                        <span class="info nested">Assert URL ...</span>
                        <div class="user-input nested">
                            Assert URL <input type="text" placeholder="url" value="${url ? url : ''}">
                            <button type="button">Update</button>
                        </div>
                    </div>
                </li>`;
    let template = document.createElement('template');
    template.innerHTML = clickElement;

    let label_btn = template.content.querySelector('.user-input button[type = "button"]');

    //Update from user input function
    template.content.querySelector('.user-input button[type = "button"]').addEventListener('click', function () {
        console.log(this);
        let label_btn = this.parentNode.parentNode.querySelector('.label-button').querySelector('button[type="button"]');
        console.log(label_btn);
        let locator = this.parentNode.querySelector('input[type="text"]').value;
        let info = this.parentNode.parentNode.querySelector('span.info');
        info.textContent = 'Assert URL ' + locator;
    });
    //Show and hide user input
    let showHideBtn = template.content.querySelector('.label-button button[type = "button"]');
    showHideBtn.addEventListener('click', function () {
        this.classList.toggle('down');
        let user_input = this.parentNode.parentNode.querySelector('.user-input');
        user_input.classList.toggle('nested');
    });

    showHideBtn.addEventListener('mouseover', function() {
        let info = this.parentNode.parentNode.querySelector('span.info');
        if (info.classList.contains('nested')) info.classList.remove('nested');
    });

    showHideBtn.addEventListener('mouseout', function () {
        let info = this.parentNode.parentNode.querySelector('span.info');
        if (!info.classList.contains('nested')) info.classList.add('nested');
    });

    return template.content.firstElementChild;

}
function addElementTextAssert(element) {
    let assertElement = createElementAssertElementText();
    let actionList = element.parentNode.parentNode.parentNode.parentNode.querySelector('.actions');
    actionList.insertAdjacentElement('beforeend', assertElement);
}
function createElementAssertElementText(existElement, text) {
    let clickElement =  `<li>
                    <div class="action" type="verify-element-text">
                        <div class="label-button">
                            <input type="checkbox" name="select" onclick="selecting(this.parentNode)">
                            <button type="button" class="up">Assert Element Text ...   </button>
                        </div>
                        <span class="info nested">Assert Element Text ...</span>
                        <div class="user-input nested">
                            Assert Element Text <input type="text" placeholder="element" value="${existElement ? existElement : ''}"> contains <input type="text" placeholder="text" value="${text ? text : ''}">
                            <button type="button">Update</button>
                        </div>
                    </div>
                </li>`;
    let template = document.createElement('template');
    template.innerHTML = clickElement;

    let label_btn = template.content.querySelector('.user-input button[type = "button"]');

    //Update from user input function
    template.content.querySelector('.user-input button[type = "button"]').addEventListener('click', function () {
        console.log(this);
        let label_btn = this.parentNode.parentNode.querySelector('.label-button').querySelector('button[type="button"]');
        console.log(label_btn);
        let text = this.parentNode.querySelector('input[placeholder="text"]').value;
        let element = this.parentNode.querySelector('input[placeholder="element"]').value;
        let info = this.parentNode.parentNode.querySelector('span.info');
        info.textContent = 'Assert Element Text ' + locator + 'contains' + text;
        // if (locator.length > 3) {
        //     label_btn.innerHTML = 'Click ' + locator.substring(0, 3) + '...';
        // } else {
        //     label_btn.innerHTML = 'Click '+ locator;
        // }
    });
    //Show and hide user input
    let showHideBtn = template.content.querySelector('.label-button button[type = "button"]');
    showHideBtn.addEventListener('click', function () {
        this.classList.toggle('down');
        let user_input = this.parentNode.parentNode.querySelector('.user-input');
        user_input.classList.toggle('nested');
    });

    showHideBtn.addEventListener('mouseover', function() {
        let info = this.parentNode.parentNode.querySelector('span.info');
        if (info.classList.contains('nested')) info.classList.remove('nested');
    });

    showHideBtn.addEventListener('mouseout', function () {
        let info = this.parentNode.parentNode.querySelector('span.info');
        if (!info.classList.contains('nested')) info.classList.add('nested');
    });

    return template.content.firstElementChild;
}

function addTextAssert(element) {
    let assertTextElement = createTextAssertElement();
    let actionList = element.parentNode.parentNode.parentNode.parentNode.querySelector('.actions');
    actionList.insertAdjacentElement('beforeend', assertTextElement);
}

function addAssertVisibleElement(element) {
    let assertElement = createAssertVisibleElement();
    let actionList = element.parentNode.parentNode.parentNode.parentNode.querySelector('.actions');
    actionList.insertAdjacentElement('beforeend', assertElement);
}
function createAssertVisibleElement(locator) {
    let clickElement =  `<li>
                    <div class="action" type="verify-element">
                        <div class="label-button">
                            <input type="checkbox" name="select" onclick="selecting(this.parentNode)">
                            <button type="button" class="up">Assert Visible Element ... </button>
                        </div>
                        <span class="info nested">Assert Visible Element ...</span>
                        <div class="user-input nested">
                            Assert Visible Element <input type="text" placeholder="locator" value="${locator ? locator : ''}">
                            <button type="button">Update</button>
                        </div>
                    </div>
                </li>`;
    let template = document.createElement('template');
    template.innerHTML = clickElement;

    let label_btn = template.content.querySelector('.user-input button[type = "button"]');

    //Update from user input function
    template.content.querySelector('.user-input button[type = "button"]').addEventListener('click', function () {
        console.log(this);
        let label_btn = this.parentNode.parentNode.querySelector('.label-button').querySelector('button[type="button"]');
        console.log(label_btn);
        let locator = this.parentNode.querySelector('input[type="text"]').value;
        let info = this.parentNode.parentNode.querySelector('span.info');
        info.textContent = 'Assert URL ' + locator;
        // if (locator.length > 3) {
        //     label_btn.innerHTML = 'Click ' + locator.substring(0, 3) + '...';
        // } else {
        //     label_btn.innerHTML = 'Click '+ locator;
        // }
    });




    //Show and hide user input
    let showHideBtn = template.content.querySelector('.label-button button[type = "button"]');
    showHideBtn.addEventListener('click', function () {
        this.classList.toggle('down');
        let user_input = this.parentNode.parentNode.querySelector('.user-input');
        user_input.classList.toggle('nested');
    });

    showHideBtn.addEventListener('mouseover', function() {
        let info = this.parentNode.parentNode.querySelector('span.info');
        if (info.classList.contains('nested')) info.classList.remove('nested');
    });

    showHideBtn.addEventListener('mouseout', function () {
        let info = this.parentNode.parentNode.querySelector('span.info');
        if (!info.classList.contains('nested')) info.classList.add('nested');
    });

    return template.content.firstElementChild;
}
function createTextAssertElement(text) {
    let clickElement =  `<li>
                    <div class="action" type="verify-text">
                        <div class="label-button">
                            <input type="checkbox" name="select" onclick="selecting(this.parentNode)">
                            <button type="button" class="up">Assert Text ... </button>
                        </div>
                        <span class="info nested">Assert Text ...</span>
                        <div class="user-input nested">
                            Assert Text <input type="text" placeholder="text" value="${text ? text : ''}">
                            <button type="button">Update</button>
                        </div>
                    </div>
                </li>`;
    let template = document.createElement('template');
    template.innerHTML = clickElement;

    let label_btn = template.content.querySelector('.user-input button[type = "button"]');

    //Update from user input function
    template.content.querySelector('.user-input button[type = "button"]').addEventListener('click', function () {
        console.log(this);
        let label_btn = this.parentNode.parentNode.querySelector('.label-button').querySelector('button[type="button"]');
        console.log(label_btn);
        let locator = this.parentNode.querySelector('input[type="text"]').value;
        let info = this.parentNode.parentNode.querySelector('span.info');
        info.textContent = 'Assert URL ' + locator;
        // if (locator.length > 3) {
        //     label_btn.innerHTML = 'Click ' + locator.substring(0, 3) + '...';
        // } else {
        //     label_btn.innerHTML = 'Click '+ locator;
        // }
    });




    //Show and hide user input
    let showHideBtn = template.content.querySelector('.label-button button[type = "button"]');
    showHideBtn.addEventListener('click', function () {
        this.classList.toggle('down');
        let user_input = this.parentNode.parentNode.querySelector('.user-input');
        user_input.classList.toggle('nested');
    });

    showHideBtn.addEventListener('mouseover', function() {
        let info = this.parentNode.parentNode.querySelector('span.info');
        if (info.classList.contains('nested')) info.classList.remove('nested');
    });

    showHideBtn.addEventListener('mouseout', function () {
        let info = this.parentNode.parentNode.querySelector('span.info');
        if (!info.classList.contains('nested')) info.classList.add('nested');
    });

    return template.content.firstElementChild;
}


function createTestTemplate() {
    let xml = `<TestSuite>`;
    let url = document.querySelector('#url');
    if (url.value === '') {
        window.alert("Empty website!");
        return;
    }
    xml = xml.concat(`<url>${url.value}</url>`);
    let validTestCase = document.querySelectorAll('.valids .testcase');
    console.log(validTestCase.length);
    validTestCase.forEach((element) => {
        xml = xml.concat('<TestCase>');
        let haveAssert = false;
        xml = xml.concat(`<Scenario>${element.querySelector('.test-case-name').value}</Scenario>`)
        let children = element.querySelector('.actions').children;
        Array.from(children).forEach(function (child) {
            console.log(child);
            if (child.firstElementChild.getAttribute('type').includes('verify')) haveAssert = true;
           xml = xml.concat(parse(child.firstElementChild));
        });
        // let actions = element.querySelectorAll('.actions .action');
        // actions.forEach((action) => {
        //     console.log(action);
        //     xml = xml.concat(parse(action));
        // });
        let responses = element.querySelectorAll('.response');
        responses.forEach(function (response) {
            xml = xml.concat(parseResponse(response));
        });
        xml = xml.concat(`<assert>${haveAssert}</assert>`);

        xml = xml.concat('</TestCase>');
    });
    xml = xml.concat('</TestSuite>');
    xml = formatXml(xml, "\t");
    let xhr = new XMLHttpRequest();
    xhr.open("POST", "/testtemplate");
    // xhr.setRequestHeader("Accept", "application/json");
    xhr.setRequestHeader("Content-Type", "application/json");
    let data = {
        "template" : xml,
    }
    xhr.onload = () => {
        if (xhr.readyState === 4 && xhr.status === 200) {
            window.location.href = '/test.html';
        } else {
            console.log(`Error: ${xhr.status}`);
        }
    };

    xhr.send(JSON.stringify(data));
    console.log(xml);
}

function selecting(element) {
    if (element.parentNode.classList.contains('selected')) {
        element.parentNode.classList.remove('selected');
        element.parentNode.querySelectorAll('input[type="checkbox"]').forEach((child) => {
              child.checked = false;
        });
        element.parentNode.querySelectorAll('.action').forEach((action) => {
            if (action.classList.contains('selected')) action.classList.remove('selected');
        });
        if (element.parentNode.classList.contains('testcase')) {
            numOfSelectedTestCase--;
            if (numOfSelectedTestCase === 0) {
                document.querySelector('.delete-testcase-btn').classList.add('d-none');
            }
            let numOfSelectedAction = element.parentNode.querySelectorAll('.selected').length;
            if (numOfSelectedAction === 0) {
                let deleteBtn = element.parentNode.querySelector('.delete-action-btn');
                if (!deleteBtn.classList.contains('d-none')) deleteBtn.classList.add('d-none');
            }

        } else {
            let testCaseElement = element;
            while (!testCaseElement.classList.contains('testcase')) testCaseElement = testCaseElement.parentNode;

            let numOfSelectedAction = testCaseElement.querySelectorAll('.selected').length;
            if (numOfSelectedAction === 0) {
                let deleteBtn = testCaseElement.querySelector('.delete-action-btn');
                if (!deleteBtn.classList.contains('d-none')) deleteBtn.classList.add('d-none');
            }
        }
    } else {
        element.parentNode.classList.add("selected");
        element.parentNode.querySelectorAll('input[type="checkbox"]').forEach((child) => {
            child.checked = true;
        })
        element.parentNode.querySelectorAll('.action').forEach((action) => {
            if (!action.classList.contains('selected')) action.classList.add('selected');
        });
        if (element.parentNode.classList.contains('testcase')) {
            numOfSelectedTestCase++;
            document.querySelector('.delete-testcase-btn').classList.remove('d-none');
            let numOfSelectedAction = element.parentNode.querySelectorAll('.selected').length;
            if (numOfSelectedAction > 0) {
                let deleteBtn = element.parentNode.querySelector('.delete-action-btn');
                deleteBtn.classList.remove('d-none');
            }
        } else {
            let testCaseElement = element;
            while (!testCaseElement.classList.contains('testcase')) testCaseElement = testCaseElement.parentNode;
            testCaseElement.querySelector('.delete-action-btn').classList.remove('d-none');
        }
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

function deleteSelectedTestCase() {
    let selectedTestCases = document.querySelectorAll('.selected.testcase');
    selectedTestCases.forEach(function (testcase) {
        testcase.remove();
    });
}

function deleteSelectedAction(element) {
    let testcase = element.parentNode.parentNode;
    let selectedActions = testcase.querySelectorAll('.selected.action, .selected.custom-container');
    console.log(selectedActions)
    selectedActions.forEach(function (action) {
        while (action.tagName !== "LI") {
            console.log(action.tagName);
            action = action.parentNode;
        }
        action.remove();
    });
}



function showAndHide(element) {
    element.parentElement.querySelector('.nested').classList.toggle("active");
    element.classList.toggle("caret-down");
}

function getExpressionInfo(element) {
    let type = element.getAttribute('type');
    if (type === 'click' || type === 'input-text') {
        return  element.querySelector('.info').textContent;
    } else if (type === 'and' || type === 'or') {
        let children = element.querySelector('.actions').children;


        let childrenString = [];

            Array.from(children).forEach(function(child){
            childrenString.push(getExpressionInfo(child.firstElementChild));
        });
        if (type === 'and') return '(' + childrenString.join(' & ') + ')';
        else return '(' + childrenString.join(' | ') + ')';
    }
}

let toggler = document.querySelectorAll('.caret');
let i = 0;


for (i = 0; i < toggler.length; i++) {
    toggler[i].addEventListener("click", function() {
        console.log(this);

    });
}


function updateExpressionAndShowInfo(element) {
    let action = element.parentNode.parentNode;
    let inforString = getExpressionInfo(action);
    let info = action.querySelector('span.info');
    info.innerText = inforString;

    let infoElement = element.parentNode.parentNode.querySelector('.info');
    console.log(infoElement);

    let haveValue = false;
    for (let i = 0; i < inforString.length; i++) {
        if (inforString.charAt(i) !== '(' && inforString.charAt(i) !== ')') {
            haveValue = true;
            break;
        }
    }

    if (haveValue) infoElement.classList.add('active');
}

function hideInfo(element) {
    let infoElement = element.parentNode.parentNode.querySelector('.info');
    console.log(infoElement);
    infoElement.classList.remove('active');
}

let numOfSelectedTestCase = 0;

window.onmousemove = function(e) {
    let tooltips = document.querySelectorAll('.info');
            tooltips.forEach(function (tooltip) {
        tooltip.style.top = (e.clientY - 30) + 'px';
        tooltip.style.left = (e.clientX - 20) + 'px';
    });
}