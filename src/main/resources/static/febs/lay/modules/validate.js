// 定义常用的校验，常用的正则 https://www.open-open.com/code/view/1430625516632
layui.define(['jquery'], function (exports) {
    var $ = layui.jquery;
    exports('validate', {
        username: function (value, item) {
            if (!isEmpty(value)) {
                var result = '';
                $.ajax({
                    url: ctx + 'user/check/' + value,
                    data: {
                        "userId": item.getAttribute('id')
                    },
                    async: false,
                    type: 'get',
                    success: function (d) {
                        (!d) && (result = '该用户名已存在')
                    }
                });
                if (!isEmpty(result)) {
                    return result;
                }
            }
        },
        cron: function (value, item) {
            if (!isEmpty(value)) {
                var result = '';
                $.ajax({
                    url: ctx + 'job/cron/check',
                    data: {
                        "cron": value
                    },
                    async: false,
                    type: 'get',
                    success: function (d) {
                        (!d) && (result = 'cron表达式不合法')
                    }
                });
                if (!isEmpty(result)) {
                    return result;
                }
            }
        },
        email: function (value) {
            if (!isEmpty(value)) {
                if (!new RegExp("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$").test(value)) {
                    return '请填写正确的邮箱';
                }
            }
        },
        phone: function (value) {
            if (!isEmpty(value)) {
                if (!new RegExp("^1\\d{10}$").test(value)) {
                    return '请填写正确的手机号码';
                }
            }
        },
        number: function (value) {
            if (!isEmpty(value)) {
                if (!new RegExp("^[0-9]*$").test(value)) {
                    return '只能填写数字';
                }
            }
        },
        range: function (value, item) {
            var minlength = 0;
            var maxlength = 200;
            var max=200;
            var length = value.length;
            if (length > max || length < minlength) {
                return '长度范围 ' + minlength + ' ~ ' + max + ' 个字符6666';
            }
        },
        /**
         * 权重
         */
        weight: function (val, item) {
            if (!/^(0.\d+|0|1)?$/.test(val)) {
                return '权值为0到1之间的小数'
            }
        }
    });

    function isEmpty(obj) {
        return typeof obj == 'undefined' || obj == null || obj === '';
    }
});