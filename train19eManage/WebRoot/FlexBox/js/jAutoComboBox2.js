(function () {
    $.extend($.fn, {
        AutoComboBox: function (op) {
            op = $.extend({
                url: false,                         //获取数据的地址     
                type: "GET",                         //请求远程数据的方式 get/post
                data: true,                         //本地数据(Json格式)  只有当url参数设置为false时该参数才生效 否则优先从远程url获取数据
                cssClass: false,                    //下拉框使用的class样式
                firstValue: [true, "0", "请选择"],  //是否自动创建第一个值  这个值通常为 请选择 ,不限等
                nullDispaly: true,                  //当正在操作的下拉框的子类为空时 他下面的子类下拉框是否隐藏 默认隐藏  如果不隐藏 建议设置firstValue 为true
                idPrefix: "zldd_ComboBox_ID",       //自动生成下拉框的 默认ID前缀
                defaultValue: false                 //如果此值不为false  将根据该值设置默认值 并且递归向上设置所有的下拉框默认值
            }, op);
            var c = this;
            if (op.url) {    //从远程获取数据
                $.ajax({
                    type: op.type,
                    url: op.url,
                    success: function (msg) {
                        op.data = eval(msg);
                        if (op.defaultValue) {
                            // alert(msg);
                            c.AutoDefaultValue(c, 1, op, op.data, op.data);
                        }
                        else {
                            c.CreateOption(c, 1, op, op.data);
                        }
                    }, error: function () {
                        alert("not connection server");
                    }
                });
            }
            else {
                if (op.data) {
                    c.CreateOption(c, 1, op, op.data);
                }
            }
        },
        /*设置默认值-start*/
        AutoDefaultValue: function (o, index, op, data, children, item_id) {
            var defaultValue = op.defaultValue;
            $.each(children, function (i, item) {
                if (item.id == defaultValue) {
                    //找到相同的ID了..   //index  代表当前的是第几个下拉框
                    //alert(children);
                    o.SetDefaultValue(o, index, op, data, children);
                    op.defaultValue = item_id;
                    children = data;
                    o.AutoDefaultValue(o, index + 1, op, data, children);
                    return false;
                }
                if (item.children) {  //存在子类
                    o.AutoDefaultValue(o, index + 1, op, data, item.children, item.id);
                }
            });
        },
        SetDefaultValue: function (o, index, op, data, children) {
            $select = $('<select id="' + op.idPrefix + index + '" index="' + index + '"/>');   //创建新的下拉框

            if (o.find('select').length > 0) {
                $select.insertBefore(o.find('select').eq(0));
            }
            else {
                o.append($select);
            }
            //附加用户自定义下拉框样式
            if (op.cssClass) {
                $select.addClass(op.cssClass);
            }
            var tempOption = '';
            if (op.firstValue[0]) {
                tempOption += "<option value='" + op.firstValue[1] + "'>" + op.firstValue[2] + "</option>"
            }
            $.each(children, function (i, item) {
                var checked = "";
                if (item.id == op.defaultValue)
                    checked = 'selected="selected"';
                tempOption += "<option value='" + item.id + "' " + checked + ">" + item.text + "</option>";
            });
            //创建联动事件
            var ComboBox = this;

            $select.change(function () {
                ComboBox.bindChangeEvent(o, index, op, data, this);
            });
            $select.empty().append(tempOption);
        },
        bindChangeEvent: function (o, index, op, children, select) {   //绑定下拉框onchange事件
            var ComboBox = this;
            var val = $(select).val();
            $.each(children, function (i, item) {
                if (item.id == val) {
                    if (item.children) {
                        var ch = '';
                        if (op.firstValue[0]) {
                            ch += "<option value='" + op.firstValue[1] + "'>" + op.firstValue[2] + "</option>"
                        }
                        $.each(item.children, function (i, item) {
                            ch += "<option value='" + item.id + "'>" + item.text + "</option>"
                        });
                        if ($(select).next('select').length > 0) {   //判断当前下拉框 后面是否有子类下拉框
                            $(select).next('select').get(0).options.length = 0;
                        }
                        else {
                            //创建新的下拉框
                            var $s = $('<select id="' + op.idPrefix + index + '" index="' + index + '"/>');
                            o.append($s);
                            //附加用户自定义下拉框样式
                            if (op.cssClass) {
                                $s.addClass(op.cssClass);
                            }
                            $s.change(function () {
                                ComboBox.bindChangeEvent(o, index, op, children, $s.get(0));
                            });
                        }
                        $(select).next('select').append($(ch));
                        while ($(select).next('select').next('select').length > 0) {
                            $(select).next('select').next('select').remove();
                        }
                        return false;
                    }
                    else {
                        while ($(select).next('select').length > 0) {
                            $(select).next('select').remove();
                        }
                    }
                }
                else {
                    if (item.children)
                        ComboBox.bindChangeEvent(o, index, op, item.children, select);
                }
            });
        },
        /*设置默认值-end*/
        CreateOption: function (o, index, op, data) {
            var $select;
            if (o.find("#" + op.idPrefix + index).length > 0) {      //查询当前递归的下拉框是否存在 如果存在则删除
                $select = o.find("#" + op.idPrefix + index);
                $select.get(0).options.length = 0;
            }
            else {
                $select = $('<select id="' + op.idPrefix + index + '" index="' + index + '"/>');   //创建新的下拉框
                o.append($select);
            }
            //附加用户自定义下拉框样式
            if (op.cssClass) {
                $select.addClass(op.cssClass);
            }

            var ComboBox = this;
            var tempOption = '';
            if (op.firstValue[0]) {
                tempOption += "<option value='" + op.firstValue[1] + "'>" + op.firstValue[2] + "</option>"
            }
            $.each(data, function (i, item) {
                tempOption += "<option value='" + item.id + "'>" + item.text + "</option>"
            });
            $select.empty().append(tempOption);
            $select.change(function () {    //绑定下拉框 onchange事件
                var val = $(this).val();
                nextIndex = parseInt($(this).attr("index")) + 1;
                var add = false;
                $.each(data, function (i, item) {
                    if (item.id == val) {
                        if (item.children) {
                            ComboBox.CreateOption(o, nextIndex, op, item.children);   //change时 回调该方法
                            add = true;
                            return false;
                        }
                    }
                });
                if (!add) {                       //是否回调了方法
                    var $selects = new Array();   //如果回调则进行下面的处理
                    var j = 0;
                    while (true) {                //寻找当前下拉框的 所有子集下拉框  并放进array
                        nextIndex = nextIndex;
                        $select = o.find("#" + op.idPrefix + nextIndex);
                        if ($select.length > 0)
                            $selects[j] = $select;
                        else
                            break;
                        j++;
                        nextIndex++;
                    }
                    if (op.nullDispaly) {            //根据用户设置  如果为空时  隐藏下拉框 则直接 remove()
                        for (var i = 0; i < $selects.length; i++) {
                            $selects[i].remove();
                        }

                    } else {                         //否则显示出 请选择等字样
                        for (var i = 0; i < $selects.length; i++) {
                            $select = $selects[i];
                            var t = "";
                            if (op.firstValue[0]) {
                                t += "<option value='" + op.firstValue[1] + "'>" + op.firstValue[2] + "</option>"
                            }
                            nextIndex = index + 1 + i;
                            var $ts = $("<select id='" + op.idPrefix + nextIndex + "' index='" + nextIndex + "'>" + t + "</select>");
                            $select.replaceWith($ts);
                            $select = $ts;
                            //附加用户自定义下拉框样式
                            if (op.cssClass) {
                                $select.addClass(op.cssClass);
                            }
                        }
                    }
                }
            });
            if ($select.val()) { //当前默认项有值 初始化下一个下啦框
                ComboBox.CreateOption_NotFirst(o, parseInt($select.attr("index")) + 1, $select.val(), op, op.data);
            }
        },
        CreateOption_NotFirst: function (o, index, val, op, data) {   //如果当前初始化的下拉框 存在子类  你懂的...
            var ComboBox = this;
            $.each(data, function (i, item) {
                if (item.id == val) {
                    if (item.children) {
                        ComboBox.CreateOption(o, index, op, item.children);   //change时 回调该方法
                        return false;
                    }
                }
                if (item.children) {
                    ComboBox.CreateOption_NotFirst(o, index, val, op, item.children);
                }
            });
        },
        ComboBoxGetValue: function (Atleast, value_text_rec, errFn) {  //返回第几级的值  默认返回最后一个下拉框的值  不过不满足条件弹出自定义错误消息
            var s = this.find("select");
            if ((Atleast) || (Atleast == 0)) {
                if (s.length - 1 >= Atleast) {
                    if (value_text_rec) {
                        switch (value_text_rec) {
                            case "value":
                                return s.eq(Atleast).val();
                            case "text":
                                return s.eq(Atleast).find('option:selected').text();
                            case "rec":
                                return s.eq(Atleast);
                        }
                    }
                    else {
                        return s.eq(Atleast).val();
                    }
                }
                else {
                    if (errFn) {
                        errFn();
                    }
                    return "";
                }
            }
            else {
                return s.eq(s.length - 1).val();
            }
        }
    });
})(jQuery);