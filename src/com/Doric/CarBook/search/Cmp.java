package com.Doric.CarBook.search;

import java.util.Comparator;
import net.sourceforge.pinyin4j.PinyinHelper;

class ComparatorCarInfo implements Comparator<CarInfor>{  
	        @Override  
	        public int compare(CarInfor o1, CarInfor o2) {  
	            return ToPinYinString(o1.getCarName()).compareTo(ToPinYinString(o2.getCarName()));  
	        }  
	          
	        private String ToPinYinString(String str){     
	            return PinyinUtil.getFullSpell(str);   
	        }
}



class ComparatorCarSeable implements Comparator<CarSeable>{  
    @Override  
    public int compare(CarSeable o1, CarSeable o2) {  
        return ToPinYinString(o1.getCarSeableName()).compareTo(ToPinYinString(o2.getCarSeableName()));  
    }  
      
    private String ToPinYinString(String str){  

    
        return PinyinUtil.getFullSpell(str);  
    }

}


