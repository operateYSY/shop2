package entity;

import entity.Fruits;

public class Tops {
	
	
	/** ��ҳ�Ƽ����� - ���� */
	public static final byte TYPE_SCROLL = 1;
	/** ��ҳ�Ƽ����� - ��ͼ */
	public static final byte TYPE_LARGE = 2;
	/** ��ҳ�Ƽ����� - Сͼ */
	public static final byte TYPE_SMALL = 3;
	
	
    private Integer id;

    private Byte type;

    private Integer goodId;
    
	private Fruits good;

    public Fruits getGood() {
		return good;
	}

	public void setGood(Fruits good) {
		this.good = good;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Integer getGoodId() {
        return goodId;
    }

    public void setGoodId(Integer goodId) {
        this.goodId = goodId;
    }
}