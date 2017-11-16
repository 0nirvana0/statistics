package com.lq.statistics;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DataFilter {
	public static List<Object[]> notStatusList = new ArrayList<Object[]>();

	public List<Object[]> getStatistic(List<Object[]> listA, List<Object[]> listB, String status) {
		List<Object[]> statisticList = new ArrayList<Object[]>();
		statisticList.add(listB.get(0));
		List<Object[]> newItemList = getNewItem(listA, listB);
		List<Object[]> notStatus = getNotStatus(listB, status);
		Set<String> notStatusNames = new HashSet<String>();
		// notStatusList.addAll(notStatus);
		for (Object[] objects : notStatus) {
			notStatusNames.add(objects[3].toString());
		}
		notStatusList.clear();
		for (String name : notStatusNames) {
			String code = "";
			for (Object[] objects : notStatus) {
				if (name.contentEquals(objects[3].toString())) {
					code += objects[1].toString() + ",";
				}
			}
			notStatusList.add(new Object[] { "", code, "", name });
		}
		notStatusList.add(new Object[] { "", "", "", "TheEnd" });

		// 剔除状态中新增重复数据
		List<String> newItemNo = new ArrayList<String>();
		for (Object[] objects : newItemList) {
			newItemNo.add(objects[1].toString());
			statisticList.add(objects);
		}
		for (Object[] objects : notStatus) {
			if (!newItemNo.contains(objects[1])) {
				statisticList.add(objects);
			}
		}

		return statisticList;
	}

	// 1，旧表（A）对比新表（B）新增的条目；
	public List<Object[]> getNewItem(List<Object[]> listA, List<Object[]> listB) {
		List<Object[]> statisticList = new ArrayList<Object[]>();
		Set<String> nameASet = new HashSet<String>();
		String name;
		// 表A的名字
		for (int i = 1; i < listA.size(); i++) {
			name = listA.get(i)[3].toString().trim().toLowerCase();
			nameASet.add(name);

		}
		for (int i = 1; i < listB.size(); i++) {
			name = listB.get(i)[3].toString().trim().toLowerCase();
			// 1，旧表（A）对比新表（B）新增的条目；
			if (!nameASet.contains(name)) {
				statisticList.add(listB.get(i));
			}
		}
		return statisticList;
	}

	public List<Object[]> getNotStatus(List<Object[]> listB, String status) {
		List<Object[]> statisticList = new ArrayList<Object[]>();
		Set<String> nameBSet = new HashSet<String>();
		String statusTemp;
		String name;
		// 表A的名字

		for (int i = 1; i < listB.size(); i++) {
			name = listB.get(i)[3].toString().trim().toLowerCase();
			statusTemp = listB.get(i)[4].toString().trim().toLowerCase();
			// 2.1未转化为指定状态的条目名字集合
			if (!statusTemp.equalsIgnoreCase(status)) {
				nameBSet.add(name);
			}
		}
		for (int i = 1; i < listB.size(); i++) {
			name = listB.get(i)[3].toString().trim().toLowerCase();
			// 2.2剔除未转化名字
			if (nameBSet.contains(name)) {
				statisticList.add(listB.get(i));
			}
		}
		return statisticList;
	}

	public List<Object[]> getNotClose(List<Object[]> allRecord) {
		List<Object[]> notClose = new ArrayList<Object[]>();
		Set<String> closeName = new HashSet<String>();
		String status;
		String creator;
		for (Object[] objects : allRecord) {
			status = objects[4].toString().trim().toLowerCase();
			if (status.contains("close")) {
				closeName.add(objects[3].toString().trim());
			}
		}
		for (Object[] objects : allRecord) {
			creator = objects[3].toString().trim();
			if (closeName.contains(creator)) {
				continue;
			}
			notClose.add(objects);
		}
		return notClose;
	}

	public List<Object[]> getNew(List<Object[]> allRecord) {
		List<Object[]> createData = new ArrayList<Object[]>();
		String status;
		createData.add(allRecord.get(0));
		allRecord.remove(0);
		for (Object[] objects : allRecord) {
			status = objects[4].toString().trim().toLowerCase();
			if (status.contains("create")) {
				createData.add(objects);
			}
		}
		allRecord.add(0, createData.get(0));
		return createData;
	}

	public List<Object[]> getD10NotClose(List<Object[]> allRecord) {
		List<Object[]> d10NotClose = new ArrayList<Object[]>();
		Set<String> closeName = new HashSet<String>();
		String status;
		String creator;
		String dateString;
		String[] date;
		LocalDate today = LocalDate.now();
		LocalDate createDate;
		d10NotClose.add(allRecord.get(0));
		allRecord.remove(0);
		for (Object[] objects : allRecord) {
			status = objects[4].toString().trim().toLowerCase();
			if (status.contains("close")) {
				closeName.add(objects[3].toString().trim());
			}
		}

		for (Object[] objects : allRecord) {
			creator = objects[3].toString().trim();
			if (closeName.contains(creator)) {
				continue;
			}
			// 判断没关闭的状态 超过10天的
			dateString = objects[2].toString().trim();
			date = dateString.split(" ")[0].split("/");
			createDate = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
			long daysDiff = ChronoUnit.DAYS.between(createDate, today);

			if (daysDiff > 10) {
				d10NotClose.add(objects);
			}

		}
		allRecord.add(0, d10NotClose.get(0));
		return d10NotClose;
	}

}
