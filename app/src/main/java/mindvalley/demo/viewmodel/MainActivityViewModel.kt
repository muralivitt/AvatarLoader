package mindvalley.demo.viewmodel

import androidx.databinding.BaseObservable
import androidx.lifecycle.MutableLiveData
import mindvalley.demo.model.Constants

class MainActivityViewModel : BaseObservable() {
    var indexValue = -1
    var imageUrl = MutableLiveData<String>()

    fun showNextImage() {
        updateImageUrl()
    }

    fun updateImageUrl(){
        imageUrl.value = Constants.imageList[getNextIndex()]
    }

    private fun getNextIndex():Int{
        indexValue = indexValue.plus(1)
        if(indexValue >= Constants.imageList.size) {
            indexValue = 0
        }
        return indexValue
    }
}