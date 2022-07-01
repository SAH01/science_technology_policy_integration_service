layui.extend({
    febs: 'lay/modules/febs',
    validate: 'lay/modules/validate',
    validate2: 'lay/modules/validate2'
}).define(['febs', 'conf'], function (exports) {
    layui.febs.initPage();
    exports('index', {});
});