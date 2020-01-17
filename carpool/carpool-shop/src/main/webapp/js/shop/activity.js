$(function () {
    $("#jqGrid").Grid({
        url: '../activity/list',
        colModel: [
            {label: 'id', name: 'id', index: 'id', key: true, hidden: true},
            {label: '活动名称', name: 'name', index: 'name', width: 120},
            {label: '发布活动者', name: 'userName', index: 'userName', width: 80},
            {label: '活动日期', name: 'dateS', index: 'dateS', width: 80},
            {label: '点赞数', name: 'collect', index: 'collect', width: 80},
            {label: '活动描述', name: 'describe', index: 'describe', width: 80},
            {
                label: '操作', width: 160, align: 'center', sortable: false, formatter: function (value, col, row) {
                    return '<button class="btn btn-outline btn-info" onclick="vm.lookDetail(' + row.id + ')"><i class="fa fa-info-circle"></i>&nbsp;详情</button>';
                }
            }]
    });
});

var vm = new Vue({
    el: '#rrapp',
    data: {
        showList: true,
        detail: false,
        showGoods: false,
        title: null,
        activity: {sendType: 0},
        ruleValidate: {
            name: [
                {required: true, message: '活动名称不能为空', trigger: 'blur'}
            ]
        },
        q: {
            name: ''
        },
        goods: [],
        goodss: [],
        user: [],
        users: [],
        selectData: {},
        sendSms: ''//是否发送短信
    },
    methods: {
        query: function () {
            vm.reload();
        },
        add: function () {
            vm.showList = false;
            vm.detail = true;
            vm.showGoods = false;
            vm.title = "新增";
            vm.activity = {sendType: 0};
        },
        update: function (event) {
            var id = getSelectedRow("#jqGrid");
            if (id == null) {
                return;
            }
            vm.showList = false;
            vm.detail = true;
            vm.showGoods = false;
            vm.title = "修改";

            vm.getInfo(id)
        },
        saveOrUpdate: function (event) {
            var url = vm.activity.id == null ? "../activity/save" : "../activity/update";

            Ajax.request({
                type: "POST",
                url: url,
                contentType: "application/json",
                params: JSON.stringify(vm.activity),
                successCallback: function (r) {
                    alert('操作成功', function (index) {
                        vm.reload();
                    });
                }
            });
        },
        del: function (event) {
            var ids = getSelectedRows("#jqGrid");
            if (ids == null) {
                return;
            }

            confirm('确定要删除选中的记录？', function () {
                Ajax.request({
                    type: "POST",
                    url: "../activity/delete",
                    contentType: "application/json",
                    params: JSON.stringify(ids),
                    successCallback: function (r) {
                        alert('操作成功', function (index) {
                            vm.reload();
                        });
                    }
                });
            });
        },
        getInfo: function (id) {
            Ajax.request({
                url: "../activity/info/" + id,
                async: true,
                successCallback: function (r) {
                    vm.activity = r.activity;
                }
            });
        },
        reload: function (event) {
            vm.showList = true;
            vm.detail = false;
            vm.showGoods = false;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {'name': vm.q.name},
                page: page
            }).trigger("reloadGrid");
            vm.handleReset('formValidate');
        },
        lookDetail: function (rowId) { //第三步：定义编辑操作
        	openWindow({
                type: 2,
                title: '<i class="fa fa-print"></i>活动详情',
                content: '../shop/activityDetail.html?id=' + rowId
            })
        },
        handleSubmit: function (name) {
            handleSubmitValidate(this, name, function () {
                vm.saveOrUpdate()
            });
        },
        handleReset: function (name) {
            handleResetForm(this, name);
        },
        publish: function (id, sendType) {
            vm.showGoods = true;
            vm.goods = [];
            vm.user = [];
            vm.getGoodss();
            vm.getUsers();
            vm.selectData = {id: id, sendType: sendType};
            vm.sendSms = false;
            openWindow({
                title: "发放",
                area: ['600px', '350px'],
                content: jQuery("#sendDiv")
            })
        },
        getUsers: function () {
            Ajax.request({
                url: "../user/queryAll",
                async: true,
                successCallback: function (r) {
                    vm.users = r.list;
                }
            });
        },
        publishSubmit: function () {

            var sendType = vm.selectData.sendType;
            if (sendType == 1 && vm.user.length == 0) {
                vm.$Message.error('请选择下发会员');
                return;
            }
            if (sendType == 3 && vm.goods.length == 0) {
                vm.$Message.error('请选择下发商品');
                return;
            }
            confirm('确定下发优惠券？', function () {
                Ajax.request({
                    type: "POST",
                    dataType: 'json',
                    url: "../activity/publish",
                    contentType: "application/json",
                    params: JSON.stringify({
                        sendType: vm.selectData.sendType,
                        activityId: vm.selectData.id,
                        goodsIds: vm.goods.toString(),
                        userIds: vm.user.toString(),
                        sendSms: vm.sendSms
                    }),
                    successCallback: function (r) {
                        alert('操作成功', function (index) {
                            vm.reload();
                            vm.showGoods = false;
                            vm.showList = true;
                        });
                    }
                });
            });
        },
        getGoodss: function () {
            Ajax.request({
                url: "../goods/queryAll/",
                async: true,
                successCallback: function (r) {
                    vm.goodss = r.list;
                }
            });
        }
    }
});