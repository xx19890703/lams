package com.suun.model.system;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/*嵌入式主键类，要满足以下几点要求。
1.必须实现Serializable接口、必须有默认的public无参数的构造方法、必须覆盖equals 和hashCode方法，这些要求与使用复合主键的要求相同。
2.将嵌入式主键类使用@Embeddable标注，表示这个是一个嵌入式类。
3.通过@EmbeddedId注释标注实体中的嵌入式主键
*/
@Embeddable 
public class Dic_datakey  implements Serializable{
	
	private static final long serialVersionUID = 301328392357249015L;

	private String dic_no;

	private String data_no;
	
	public Dic_datakey(){
		
	}
	//@ManyToOne
	@Column(length=30)
	public String getDic_no() {
		return dic_no;
	}

	public void setDic_no(String dic_no) {
		this.dic_no = dic_no;
	}

	public void setData_no(String data_no) {
		this.data_no = data_no;
	}
	@Column(length=30)
	public String getData_no() {
		return data_no;
	}
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
 
        Dic_datakey that = (Dic_datakey) o;
 
        if (dic_no != null ? !dic_no.equals(that.dic_no) : that.dic_no != null) 
        	return false;
        if (data_no != null ? !data_no.equals(that.data_no) : that.data_no != null)
            return false;
 
        return true;
    }
	@Override
    public int hashCode() {
        int result;
        result = (dic_no != null ? dic_no.hashCode() : 0);
        result = 31 * result + (data_no != null ? data_no.hashCode() : 0);
        return result;
    }

}
