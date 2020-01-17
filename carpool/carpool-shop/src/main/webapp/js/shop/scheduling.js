$(function () {
    $("#jqGrid").Grid({
        url: '../scheduling/list',
        colModel: [
            {label: 'id', name: 'id', index: 'id', key: true, hidden: true},
            {label: '发布者', name: 'userName', index: 'userName', width: 120},
            {label: '发布时间', name: 'createTime', index: 'createTime', width: 110},
            {label: '行程周期', name: 'schedulingCycle', index: 'schedulingCycle', width: 120},
            {label: '行程到达时间', name: 'arriveTimeS', index: 'arriveTimeS', width: 100},
            {label: '始发地', name: 'fromAddressVo.addressName', index: 'fromAddressVo.addressName', width: 80},
            {label: '目的地', name: 'toAddressVo.addressName', index: 'toAddressVo.addressName', width: 80},
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
        scheduling: {sendType: 0},
        ruleValidate: {
        	createTime: [
                {required: true, message: '名称不能为空', trigger: 'blur'}
            ]
        },
        q: {
            createTime: ''
        },
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
            vm.scheduling = {sendType: 0};
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
            var url = vm.scheduling.id == null ? "../scheduling/save" : "../scheduling/update";

            Ajax.request({
                type: "POST",
                url: url,
                contentType: "application/json",
                params: JSON.stringify(vm.scheduling),
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
                    url: "../scheduling/delete",
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
                url: "../scheduling/info/" + id,
                async: true,
                successCallback: function (r) {
                    vm.scheduling = r.scheduling;
                }
            });
        },
        reload: function (event) {
            vm.showList = true;
            vm.detail = false;
            vm.showGoods = false;
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {'createTime': vm.q.createTime},
                page: page
            }).trigger("reloadGrid");
        },
        lookDetail: function (rowId) { //第三步：定义编辑操作
        	openWindow({
                type: 2,
                title: '<i class="fa fa-print"></i>活动详情',
                content: '../shop/schedulingDetail.html?id=' + rowId
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
                    url: "../scheduling/publish",
                    contentType: "application/json",
                    params: JSON.stringify({
                        sendType: vm.selectData.sendType,
                        schedulingId: vm.selectData.id,
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