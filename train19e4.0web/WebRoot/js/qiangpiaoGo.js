//获取n天后的日期
function adddays(n) {
	var newdate = new Date();
	var newtimems = newdate.getTime() + (n * 24 * 60 * 60 * 1000);
	newdate.setTime(newtimems);
	var month = newdate.getMonth() + 1;
	month < 10 ? month = '0' + month : month = month;
	var day = newdate.getDate();
	day < 10 ? day = '0' + day : day = day;
	// 只得到年月日的
	var newDay = newdate.getFullYear() + "-" + month + "-" + day;
	return newDay;
}
// 出发日期--日历控件
function myfunction(element) {
	// var arr = new Array(adddays(20), adddays(21), adddays(22), adddays(23),
	// adddays(24), adddays(25), adddays(26), adddays(27), adddays(28),
	// adddays(29));
	// WdatePicker({doubleCalendar: true
	// ,dateFmt:'yyyy-MM-dd',minDate:'%y-%M-%d',maxDate:'%y-%M-{%d+29}',
	// specialDates:arr});
	var m = document.getElementById("book_day_num").value;
	// 去除 加10天预约购票服务
	var i = parseInt(m);
	// var j=i-1+10;
	var j = i - 1;
	var arr = new Array(adddays(parseInt(i)), adddays(parseInt(i) + 1),
			adddays(parseInt(i) + 2), adddays(parseInt(i) + 3),
			adddays(parseInt(i) + 4), adddays(parseInt(i) + 5),
			adddays(parseInt(i) + 6), adddays(parseInt(i) + 7),
			adddays(parseInt(i) + 8), adddays(parseInt(i) + 9));
	WdatePicker({
		doubleCalendar : true,
		dateFmt : 'yyyy-MM-dd',
		minDate : '%y-%M-%d',
		maxDate : '%y-%M-{%d+' + j + '}'
	});

}

$()
		.ready(
				function() {
					$(".show_li")
							.live(
									'click',
									function() {// 点击日期，查询车次
										// 消息框
										var dialog = art.dialog({
											lock : true,
											fixed : true,
											left : '50%',
											top : '250px',
											title : 'Loading...',
											icon : "/images/loading.gif",
											content : '正在查询，请稍候！'
										});
										var id = $(".current_choose")
												.attr("id");
										$("#" + id).removeClass(
												"current_choose");// 去除当前样式
										$(this).attr("class",
												"show_li current_choose");// 当前样式
										var str = $.trim($(this).text());// 11-18
																			// 周三
										// alert(str);
										if ($.trim($("#fromZh").val()) == ""
												|| $.trim($("#fromZh").val()) == "出发城市") {
											showErrMsg("fromZh", "110px",
													"请输入出发城市！");
											return;
										} else {
											hideErrMsg("fromZh");
										}
										if ($.trim($("#toZh").val()) == ""
												|| $.trim($("#toZh").val()) == "到达城市") {
											showErrMsg("toZh", "110px",
													"请输入到达城市！");
											return;
										} else {
											hideErrMsg("toZh");
										}
										var newId = $(".current_choose").attr(
												"id").substring(3);
										var date = $("#li_span_" + newId).val();
										$("#travel_time").val(date);
										if ($("#travel_time").val() == "") {
											showErrMsg("travel_time", "110px",
													"请输入出发日期！");
											return;
										} else {
											hideErrMsg("travel_time");
										}

										$(".aui_titleBar").hide();
										$("form:first").submit();

									});

					var animateAuto = function(prop, speed, callback) {
						var elem, height, width;
						return this.each(function(i, el) {
							el = jQuery(el), elem = el.clone().css({
								"height" : "auto",
								"width" : "auto"
							}).appendTo("body");
							height = elem.css("height"), width = elem
									.css("width"), elem.remove();
							if (prop === "height")
								el.animate({
									"height" : height
								}, speed, callback);
							else if (prop === "width")
								el.animate({
									"width" : width
								}, speed, callback);
							else if (prop === "both")
								el.animate({
									"width" : width,
									"height" : height
								}, speed, callback);
						});
					}

					// 途经站
					$(".wayStastion")
							.click(
									function() {
										var chufa = $(this).children("div")
												.attr("class");
										var checi = $(this).children("strong")
												.attr("id");
										if (chufa == "up-arrow") {
											$("#train_station_" + checi).hide();
											$(this).children("div").attr(
													"class", "down-arrow");
											$(this)
													.parents("tr")
													.children("td")
													.attr("style",
															"border-bottom:1px;");
											$(this).parents("tr")
													.children("td").removeAttr(
															"style");
										} else {
											$(".up-arrow").each(
													function() {
														$(this).attr("class",
																"down-arrow");
													});
											$(this).children("div").attr(
													"class", "up-arrow");
											$(this)
													.parents("tr")
													.children("td")
													.attr("style",
															"border-bottom:0px;");
											var stationame = $(this).children(
													"div").val().split("_");
											var fromName = stationame[0];
											var toName = stationame[1];
											if ($("#dis_" + checi).val() != undefined
													&& $("#dis_" + checi).val() != null) {
												$(".train_station").each(
														function() {
															$(this).hide();
														});
												// $("#train_station_"+checi).fadeTo("slow",1);
												$("#train_station_" + checi)
														.show();
											} else {
												$
														.ajax({
															url : "/station/subwayName.jhtml?checi="
																	+ $(this)
																			.children(
																					"strong")
																			.text(),
															type : "POST",
															cache : false,
															dataType : "json",
															async : true,
															success : function(
																	data) {
																if (data == ""
																		|| data == null) {
																	return false;
																}
																var size = data.length;
																for (var i = 0; i < size; i++) {
																	$(
																			"#station_con_"
																					+ checi)
																			.append(
																					"<div id=\"dis_"
																							+ checi
																							+ "\" value=\""
																							+ checi
																							+ "\" style=\"display:none\"></div>");

																	if (fromName == data[i].name
																			|| toName == data[i].name) {
																		$(
																				"#station_tbody_"
																						+ checi)
																				.append(
																						"<tr class=\"bgc\" id=\"tr_"
																								+ i
																								+ checi
																								+ "\"></tr>");
																		$(
																				"#tr_"
																						+ i
																						+ checi)
																				.append(
																						"<td style=\"width:80px;\">"
																								+ data[i].stationno
																								+ "</td>"
																								+ "<td style=\"width:120px;\">"
																								+ data[i].name
																								+ "</td>"
																								+ "<td style=\"width:90px;\">"
																								+ data[i].costtime
																								+ "</td>"
																								+ "<td style=\"width:110px;\">"
																								+ data[i].arrtime
																								+ "</td>"
																								+ "<td style=\"width:110px;\">"
																								+ data[i].starttime
																								+ "</td>"
																								+ "<td style=\"width:110px;\">"
																								+ data[i].interval
																								+ "</td>"
																								+ "<td style=\"width:110px;\">"
																								+ data[i].distance
																								+ "</td>");
																	} else {
																		$(
																				"#station_tbody_"
																						+ checi)
																				.append(
																						"<tr id=\"tr_"
																								+ i
																								+ checi
																								+ "\"></tr>");
																		$(
																				"#tr_"
																						+ i
																						+ checi)
																				.append(
																						"<td style=\"width:80px;\">"
																								+ data[i].stationno
																								+ "</td>"
																								+ "<td style=\"width:120px;\">"
																								+ data[i].name
																								+ "</td>"
																								+ "<td style=\"width:90px;\">"
																								+ data[i].costtime
																								+ "</td>"
																								+ "<td style=\"width:110px;\">"
																								+ data[i].arrtime
																								+ "</td>"
																								+ "<td style=\"width:110px;\">"
																								+ data[i].starttime
																								+ "</td>"
																								+ "<td style=\"width:110px;\">"
																								+ data[i].interval
																								+ "</td>"
																								+ "<td style=\"width:110px;\">"
																								+ data[i].distance
																								+ "</td>");
																	}
																}
																$(
																		".train_station")
																		.each(
																				function() {
																					$(
																							this)
																							.hide();
																				});
																// 动态增加显示途经站区域高度
																$(
																		"#train_station_"
																				+ checi)
																		.show();
																var down = $(
																		"#station_con_"
																				+ checi)
																		.height();
																changeHeight(down + 20);
															}
														});
											}
										}
									});

					// 更多筛选条件
					$("#ser-more").click(function() {
						if ($(this).attr("class") == "down-arrow") {
							$("#toStation").show();
							$("#toTime").show();
							$("#startAfter").show();
							$(this).attr("class", "up-arrow");
						} else {
							$("#toStation").hide();
							$("#toTime").hide();
							$("#startAfter").hide();
							$(this).attr("class", "down-arrow");
						}
					});
					/** 筛选功能初始化数组 */
					var arrType = new Array();
					var arrTime = new Array();
					var toTime = new Array();
					var arrStation = new Array();
					var toStation = new Array();

					/** 初始化发车站、到达车站信息* */
					var arrStationName = new Array();
					var toStationName = new Array();
					var count = $("#train_total").text();
					if (count != 0) {
						/** 发车站信息 */
						$(".startname").each(function() {
							var value = $(this).text();
							var bo = false;
							if (arrStationName.length == 0) {
								arrStationName.push(value);
							}
							for (var i = 0; i < arrStationName.length; i++) {
								if (value == arrStationName[i]) {
									bo = true;
								}
							}
							if (!bo) {
								arrStationName.push(value);
							}
						});
						if (arrStationName.length >= 2) {
							$("#fromStation").append("<strong>发站选择：</strong>");
							$("#fromStation")
									.append(
											"<label id=\"selectFromStation\" class=\"choose_all\" for=\"sx_cb1\">全选</label>");
							for (var i = 0; i < arrStationName.length; i++) {
								$("#fromStation")
										.append(
												"<span><input type=\"checkbox\" id=\"fromStation_"
														+ arrStationName[i]
														+ "\" class=\"filter fromStationName\" value=\""
														+ arrStationName[i]
														+ "\" filter=\"s\"><label for=\"fromStation_"
														+ arrStationName[i]
														+ "\">"
														+ arrStationName[i]
														+ "</label></span>");
							}
						} else {
							$("#fromStation").remove();
						}

						/** 到达站xinx */
						$(".endname").each(function() {
							var value = $(this).text();
							var bo = false;
							if (toStationName.length == 0) {
								toStationName.push(value);
							}
							for (var i = 0; i < toStationName.length; i++) {
								if (value == toStationName[i]) {
									bo = true;
								}
							}
							if (!bo) {
								toStationName.push(value);
							}
						});
						if (toStationName.length >= 2) {
							$("#toStation").append("<strong>到站选择：</strong>");
							$("#toStation")
									.append(
											"<label id=\"selectToStation\" class=\"choose_all\" for=\"sx_cb1\">全选</label>");
							for (var i = 0; i < toStationName.length; i++) {
								$("#toStation")
										.append(
												"<span><input type=\"checkbox\" id=\"toStation_"
														+ toStationName[i]
														+ "\" class=\"filter toStationName\" value=\""
														+ toStationName[i]
														+ "\" filter=\"s\"><label for=\"toStation_"
														+ toStationName[i]
														+ "\">"
														+ toStationName[i]
														+ "</label></span>");
							}
						} else {
							$("#toStation").remove();
						}
					} else {
						$("#fromStation").remove();
						$("#toStation").remove();
					}
					/** 车次全选* */
					$("#selectALLTrain").click(
							function() {
								var len = arrType.length;
								if (len == 8) {
									$("li#trainType").find(":checkbox").attr(
											"checked", false);
								} else {
									$("li#trainType").find(":checkbox").attr(
											"checked", true);
								}

								totalChoose();
							});

					/** 车次过滤器* */
					$(":checkbox.trainType").click(function() {
						totalChoose();
					});
					/** 发车时间全选* */
					$("#selectALLTime").click(
							function() {
								var len = arrTime.length;
								if (len == 4) {
									$("li#fromTime").find(":checkbox").attr(
											"checked", false);
								} else {
									$("li#fromTime").find(":checkbox").attr(
											"checked", true);
								}

								totalChoose();
							});

					/** 发车时间过滤器* */
					$(":checkbox.startTime").click(function() {
						totalChoose();
					});

					/** 到站时间全选* */
					$("#selectALLTo").click(
							function() {
								var len = toTime.length;
								if (len == 4) {
									$("li#toTime").find(":checkbox").attr(
											"checked", false);
								} else {
									$("li#toTime").find(":checkbox").attr(
											"checked", true);
								}

								totalChoose();
							});

					/** 到站时间过滤器* */
					$(":checkbox.arriveTime").click(function() {
						totalChoose();
					});

					/** 发车站过滤器* */
					$(":checkbox.fromStationName").click(function() {
						totalChoose();

					});
					/** 发车站全选* */
					$("#selectFromStation").click(
							function() {
								var len = arrStation.length;
								if (len == arrStationName.length) {
									$("li#fromStation").find(":checkbox").attr(
											"checked", false);
								} else {
									$("li#fromStation").find(":checkbox").attr(
											"checked", true);
								}
								totalChoose();
							});
					// /发车站筛选
					function fromStationChoose() {
						arrStation = new Array();
						$(":checkbox.fromStationName:checked").each(function() {
							var station = $(this).val();
							arrStation.push(station);
						});
						$(".startname").each(function() {
							var id = $(this).attr("id");
							var value = $("#" + id).text();
							var stationShow = false;
							for (var num = 0; num < arrStation.length; num++) {
								stationShow = true;
								if (value == arrStation[num]) {
									stationShow = false;
									break;
								}
							}
							if (stationShow) {
								$(this).parents("tr").hide();
							}
						});
					}

					/** 到站过滤器* */
					$(":checkbox.toStationName").click(function() {
						totalChoose();
					});
					/** 到站全选* */
					$("#selectToStation").click(
							function() {
								var len = toStation.length;
								if (len == toStationName.length) {
									$("li#toStation").find(":checkbox").attr(
											"checked", false);
								} else {
									$("li#toStation").find(":checkbox").attr(
											"checked", true);
								}

								totalChoose();
							});
					// /到站筛选
					function toStataionChoose() {
						toStation = new Array();
						$(":checkbox.toStationName:checked").each(function() {
							var station = $(this).val();
							toStation.push(station);
						});
						$(".endname").each(function() {
							var id = $(this).attr("id");
							var value = $("#" + id).text();
							var stationShow = false;
							for (var num = 0; num < toStation.length; num++) {
								stationShow = true;
								if (value == toStation[num]) {
									stationShow = false;
									break;
								}
							}
							if (stationShow) {
								$(this).parents("tr").hide();
							}
						});
					}

					/** 可否预订全选* */
					$("#selectStartAfter").click(
							function() {
								var this_selectAfter = new Array();
								$(":checkbox.weatherBook:checked").each(
										function() {
											var station = $(this).val();
											this_selectAfter.push(station);

										});
								if (this_selectAfter.length == 2) {
									$("li#startAfter").find(":checkbox").attr(
											"checked", false);
								} else {
									$("li#startAfter").find(":checkbox").attr(
											"checked", true);
								}
								totalChoose();
							});
					/** 可否预订过滤器* */
					$(":checkbox.weatherBook").click(function() {
						totalChoose();
					});
					// /可否预订筛选
					function selectAfterChoose() {
						var selectAfter = new Array();
						$(":checkbox.weatherBook:checked").each(function() {
							var station = $(this).val();
							selectAfter.push(station);

						});
						if (selectAfter.length == 2 || selectAfter.length == 0) {
							for (var i = yuding.length - 1; i >= 0; i--) {
								$(yuding[i]).show();
							}
							for (var i = qiangpiao.length - 1; i >= 0; i--) {
								$(qiangpiao[i]).show();
							}
						} else {
							if (selectAfter[0] == "预 订") {
								for (var i = yuding.length - 1; i >= 0; i--) {
									$(yuding[i]).show();
								}
								for (var i = qiangpiao.length - 1; i >= 0; i--) {
									$(qiangpiao[i]).hide();
								}
							} else {
								for (var i = qiangpiao.length - 1; i >= 0; i--) {
									$(qiangpiao[i]).show();
								}
								for (var i = yuding.length - 1; i >= 0; i--) {
									$(yuding[i]).hide();
								}

							}
						}
						$("td.seat").each(function() {
							if ($(this).children(":visible").length == 0) {
								$(this).parents("tr").hide();
							}
						});

					}
					var yuding;
					var qiangpiao;
					yuding = new Array();
					qiangpiao = new Array();
					$(".bookTicket")
							.each(
									function() {
										var id = $(this).attr("id");
										var value = $(this).val();
										var td_this = $(this).parent().parent()[0];
										var tr_this = $(this).parents("tr")[0];
										var seat_td = $(tr_this).children(
												"td.seat")[0];
										var p = $(this).parent()[0];
										var p_index;
										$(td_this).children("p").each(
												function(index, element) {
													if (p == element) {
														p_index = index;
														return false;
													}
												});
										
										if (id) {
											console.log("有ID-"+this.tagName);
											yuding.push($(seat_td).children(
													"p:eq(" + p_index + ")"));
											yuding.push($(this).parents("p"));
										} else{
											console.log("无ID-"+this.tagName);
											qiangpiao.push($(seat_td).children(
													"p:eq(" + p_index + ")"));
											qiangpiao
													.push($(this).parents("p"));
											var tr = $(this).parents("tr");
											var travelTime = $("#travel_time").val();
											var trainCode = $(tr.find(".checi")[0]).text();
											var startCity = $(tr.find(".startname")[0]).text();
											var endCity = $(tr.find(".endname")[0]).text();
											var startTime = $(tr.find("td")[2]).children("span").text();
											var endTime = $(tr.find("td")[2]).text().substring(5);
											var costTime_str = $(tr.find("td")[3]).text();
											var defaultSelect = $(tr.find(".seat")[0]).children("p:eq(" + p_index + ")")
													.text().split("￥")[0];
											var seatMsg = $("#seatMsg_" + trainCode).val();
											costTime_str = costTime_str.replace("分", "");
											var minuteCos = costTime_str.split("小时");
											var costTime = parseInt(minuteCos[0]) * 60 + parseInt(minuteCos[1]);
											var is_buyable = "11";
											if (is_buyable == "00") {
												alert("对不起，我们真的好忙，别着急等会再订吧！\nSorry,we are very busy now,please wait a minute!");
												return false;
											}
											var url = "http://ehcp.19e.cn/rob/robTickect.jhtml?travelTime=" + travelTime + "&trainCode="
													+ trainCode + "&startCity=" + startCity + "&endCity=" + endCity
													+ "&startTime=" + startTime + "&endTime=" + endTime + "&costTime="
													+ costTime + "&seatMsg=" + seatMsg + "&defaultSelect="
													+ defaultSelect;
											console.log("qiangpiaogo"+url);
											this.href = encodeURI(url);
										}

									});

					// 车次、发车时间、到站时间筛选
					function filterChoose() {
						arrTime = new Array();
						toTime = new Array();
						arrType = new Array();
						var count = $(":checkbox.filter:checked").length;
						if (count == 0) {
							$(".trainBar").show();
						} else {
							$(".trainBar").hide();
						}
						// 车次类型
						$(":checkbox.trainType:checked").each(function() {
							var type = $(this).val();
							arrType.push(type);
						});
						// 发车时间
						$(":checkbox.startTime:checked").each(function() {
							var time = $(this).val();
							arrTime.push(time);
						});

						if ($("#ser-more").attr("class") == "up-arrow") {
							// 到站时间
							$(":checkbox.arriveTime:checked").each(function() {
								var time = $(this).val();
								toTime.push(time);
							});
						}
						$(".trainBar")
								.each(
										function() {
											var id = $(this).attr("id");
											var typeShow = true;
											for (var num = 0; num < arrType.length; num++) {
												typeShow = false;
												if ((arrType[num].split("/").length > 1)) {
													if (arrType[num].split("/")[1] == 'PT') {
														var patrn = /((ticket\_)[0-9])/;
														if (id
																.indexOf("ticket_"
																		+ arrType[num]
																				.split("/")[0]) > 0
																|| id
																		.match(patrn)) {
															typeShow = true;
															break;
														}
													} else {
														if (id
																.indexOf("ticket_"
																		+ arrType[num]
																				.split("/")[0]) > 0
																|| id
																		.indexOf("ticket_"
																				+ arrType[num]
																						.split("/")[1]) > 0) {
															typeShow = true;
															break;
														}
													}
												} else {
													if (id.indexOf("ticket_"
															+ arrType[num]) > 0) {
														typeShow = true;
														break;
													}
												}
											}
											var timeShow = true;
											for (var num = 0; num < arrTime.length; num++) {
												timeShow = false;
												if (id.indexOf(arrTime[num]) > 0) {
													timeShow = true;
													break;
												}
											}

											var toTimeShow = true;
											for (var num = 0; num < toTime.length; num++) {
												toTimeShow = false;

												if (id.indexOf(toTime[num]) > 0) {
													toTimeShow = true;
													break;
												}
											}
											if (typeShow && timeShow
													&& toTimeShow) {
												$("#" + id).show();
											}
										});
					}
					function totalChoose() {
						filterChoose();
						fromStationChoose();
						if ($("#ser-more").attr("class") == "up-arrow") {
							toStataionChoose();
							selectAfterChoose();
						}
						$(".train_station").each(function() {
							$(this).hide();
						});
						$(".trainBar:visible").each(
								function() {
									var chufa = $(this)
											.children(".wayStastion").children(
													"div").attr("class");
									var checi = $(this)
											.children(".wayStastion").children(
													"strong").attr("id");
									if (chufa == "up-arrow") {
										// $("#train_station_"+checi).fadeTo("slow",1);
										$("#train_station_" + checi).show();
									}
								});
						$("#train_total").text($(".trainBar:visible").length);
					}
					/** 筛选功能结束 */

					$("#query")
							.click(
									function() {

										if ($.trim($("#fromZh").val()) == ""
												|| $.trim($("#fromZh").val()) == "出发城市") {
											showErrMsg("fromZh", "110px",
													"请输入出发城市！");
											return;
										} else {
											hideErrMsg("fromZh");
										}
										if ($.trim($("#toZh").val()) == ""
												|| $.trim($("#toZh").val()) == "到达城市") {
											showErrMsg("toZh", "110px",
													"请输入到达城市！");
											return;
										} else {
											hideErrMsg("toZh");
										}
										if ($("#travel_time").val() == "") {
											showErrMsg("travel_time", "110px",
													"请输入出发日期！");
											return;
										} else {
											hideErrMsg("travel_time");
										}

										// 消息框
										var dialog = art.dialog({
											lock : true,
											fixed : true,
											left : '50%',
											top : '250px',
											title : 'Loading...',
											icon : "/images/loading.gif",
											content : '正在查询，请稍候！'
										});
										$(".aui_titleBar").hide();
										$("form:first").submit();
									});

					function showErrMsg(id, _width, msg) {
						$("#" + id + "_errMsg").remove();
						var offset = $("#" + id).offset();
						$obj = $("#tip").clone().attr("id", id + "_errMsg")
								.css({
									'position' : 'absolute',
									'top' : offset.top - 30,
									'left' : offset.left,
									'width' : _width
								}).appendTo("body");
						$obj.find(".errMsg").text(msg).end().show();
					}

					function hideErrMsg(id) {
						$("#" + id + "_errMsg").remove();
					}
					
					function qiangpiaogo(obj, index) {
						var tr = $(obj).parents("tr");
						var travelTime = $("#travel_time").val();
						var trainCode = $(tr.find(".checi")[0]).text();
						var startCity = $(tr.find(".startname")[0]).text();
						var endCity = $(tr.find(".endname")[0]).text();
						var startTime = $(tr.find("td")[2]).children("span").text();
						var endTime = $(tr.find("td")[2]).text().substring(5);
						var costTime_str = $(tr.find("td")[3]).text();
						var defaultSelect = $(tr.find(".seat")[0]).children("p:eq(" + index + ")")
								.text().split("￥")[0];
						var seatMsg = $("#seatMsg_" + trainCode).val();
						costTime_str = costTime_str.replace("分", "");
						var minuteCos = costTime_str.split("小时");
						var costTime = parseInt(minuteCos[0]) * 60 + parseInt(minuteCos[1]);
						var is_buyable = "11";
						if (is_buyable == "00") {
							alert("对不起，我们真的好忙，别着急等会再订吧！\nSorry,we are very busy now,please wait a minute!");
							return false;
						}
						var url = "/rob/robTickect.jhtml?travelTime=" + travelTime + "&trainCode="
								+ trainCode + "&startCity=" + startCity + "&endCity=" + endCity
								+ "&startTime=" + startTime + "&endTime=" + endTime + "&costTime="
								+ costTime + "&seatMsg=" + seatMsg + "&defaultSelect="
								+ defaultSelect;
						console.log("qiangpiaogo"+url);
						window.location = encodeURI(url);
					}

					
					
				});

// 进入下单页面
function gotoBookOrder(travelTime, trainCode, startCity, endCity, startTime,
		endTime, costTime, defaultSelect, type) {
	console.log("gotoBookOrder"+type);
	try {
		var is_buyable = "${is_buyable}";
		var seatMsg = $("#seatMsg_" + trainCode).val();
		if (is_buyable == "00") {
			alert("对不起，我们真的好忙，别着急等会再订吧！\nSorry,we are very busy now,please wait a minute!");
			return false;
		}
		var url = "/buyTicket/gotoBookOrder.jhtml?travelTime=" + travelTime
				+ "&trainCode=" + trainCode + "&startCity=" + startCity
				+ "&endCity=" + endCity + "&startTime=" + startTime + "&endTime="
				+ endTime + "&costTime=" + costTime + "&seatMsg=" + seatMsg
				+ "&defaultSelect=" + defaultSelect;
		if (type == "JL") {
			url = "/rob/robTickect.jhtml?travelTime=" + travelTime + "&trainCode="
					+ trainCode + "&startCity=" + startCity + "&endCity=" + endCity
					+ "&startTime=" + startTime + "&endTime=" + endTime
					+ "&costTime=" + costTime + "&seatMsg=" + seatMsg
					+ "&defaultSelect=" + defaultSelect;
			console.log("gotoBookOrder"+url);
		}
		window.location = encodeURI(url);
	} catch (e) {
		alert(e);
		console.log(e);
	}
	
}
function gotoRobOrder(travelTime, trainCode, startCity, endCity, startTime,
		endTime, costTime, defaultSelect, type) {
	console.log("gotoRobOrder"+type);
	try {
		var is_buyable = "${is_buyable}";
		var seatMsg = $("#seatMsg_" + trainCode).val();
		if (is_buyable == "00") {
			alert("对不起，我们真的好忙，别着急等会再订吧！\nSorry,we are very busy now,please wait a minute!");
			return false;
		}
		
		url = "/rob/robTickect.jhtml?travelTime=" + travelTime + "&trainCode="
		+ trainCode + "&startCity=" + startCity + "&endCity=" + endCity
		+ "&startTime=" + startTime + "&endTime=" + endTime
		+ "&costTime=" + costTime + "&seatMsg=" + seatMsg
		+ "&defaultSelect=" + defaultSelect;
		console.log("gotoRobOrder"+url);
		window.location = encodeURI(url);
	} catch (e) {
		alert(e);
		console.log(e);
	}
	
}
// 切换出发和到达城市
function swapCity() {
	var fromCity = $("#fromZh").val().replace("出发城市", "");
	var toCity = $("#toZh").val().replace("到达城市", "");
	$("#fromZh").val(toCity);
	$("#toZh").val(fromCity);
}
