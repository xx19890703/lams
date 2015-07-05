package com.suun.model.system.demo;

import java.io.Serializable;
/*
1.必须实现Serializable接口。
2.必须有默认的public无参数的构造方法。
3.必须覆盖equals和hashCode方法。equals方法用于判断两个对象是否相同，EntityManger通过find方法来查找Entity时，是根据equals的返回值来判断的。本例中，只有对象的name和email值完全相同时或同一个对象时则返回true，否则返回false。hashCode方法返回当前对象的哈希码，生成的hashCode相同的概率越小越好，算法可以进行优化。
4.通过@IdClass注释在实体中标注复合主键,实体中同时标注主键的属性。本例中在mid和did的getter方法前标注@Id
*/

public class DemoDetailkey  implements Serializable{

	private static final long serialVersionUID = 5602765007857381172L;

	private Long mid;

	private Long did;
	
	public DemoDetailkey(){
		
	}

	public Long getMid() {
		return mid;
	}

	public void setMid(Long mid) {
		this.mid = mid;
	}

	public Long getDid() {
		return did;
	}

	public void setDid(Long did) {
		this.did = did;
	}
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
 
        DemoDetailkey that = (DemoDetailkey) o;
 
        if (mid != null ? !mid.equals(that.mid) : that.mid != null) 
        	return false;
        if (did != null ? !did.equals(that.did) : that.did != null)
            return false;
 
        return true;
    }
	@Override
    public int hashCode() {
        int result;
        result = (mid != null ? mid.hashCode() : 0);
        result = 31 * result + (did != null ? did.hashCode() : 0);
        return result;
    }

}
