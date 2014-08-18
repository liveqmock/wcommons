/**
 * 
 */
package wcommons.httputils;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 11:19:31 AM Nov 25, 2013
 */
public class User {

	private Long id;

	private String userName;

	private int age;

	private byte status;

	private Float money;

	private Date birthday;

	private Timestamp createTime;

	private String[] interests;

	private Long[] ids;

	private Group group;

	private double m;

	private Calendar c;

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public Float getMoney() {
		return money;
	}

	public void setMoney(Float money) {
		this.money = money;
	}

	public Calendar getC() {
		return c;
	}

	public void setC(Calendar c) {
		this.c = c;
	}

	public double getM() {
		return m;
	}

	public void setM(double m) {
		this.m = m;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String[] getInterests() {
		return interests;
	}

	public void setInterests(String[] interests) {
		this.interests = interests;
	}

	public Long[] getIds() {
		return ids;
	}

	public void setIds(Long[] ids) {
		this.ids = ids;
	}

}
