## Kotlin

# 1. Setting up project
Add plugins and dependencies to `build.gradle (app)`

Tip:
* Define a version variable `def nav_version = "2.2.2"`
* Use it in the dependencies `implementation "androidx.navigation:navigation-fragment-ktx:$nav_version"`


# 2. Setting up Navigation
Tip:
* Follow [docs](https://developer.android.com/guide/navigation/navigation-getting-started) getting started and should be fine :)
* SafeArgs is the one responsible for generating Directions class
* Ensure `androidX` is set to true in `gradle.properties`

# 3. Data binding
Data Binding is a concept used to connect layout to activity/fragment (acts as the glue). 

Binding the view to the layout allows you to remove the findViewById method calls which can be potentially intensive, while binding data to the layout allows us to use data variable in the layout.

Tip:
* must have layout tag in the xml file (wrap it in `<layout></layout>`)
* Ensure `dataBinding` is set to enabled in `build.gradle` (not too sure)

#### View Binding vs Data Binding

Both generate binding classes that can be used to reference views directly (connect Layout to Activity/Fragment at compile time)

In the `Fragment` Class, use `DataBindingUtil`
```kotlin
binding = DataBindingUtil.inflate(inflater,  R.layout.some_fragment, container, false)

binding.nameText.text = "rongying"

binding.button.setOnClickListener { //some function 
}
```

|View Binding | Criteria | Data Binding |
|--|--|--|
|have to set `viewBinding` to enabled in `build.gradle` | Set up | have to set `dataBinding` to enabled in `build.gradle`|
|Faster|Compilation|Slower due to annotation processing|
|Does not require specially tagged XML layout files|Ease of use|Require more set up in XML|
|Does not support (cannot declare dynamic UI content)|Layout Variables or Expressions|Supports|
|Does not support|Two way binding|Supports|

(ok i didn't even know there was a difference. I only knew binding. lol.)


# 4. View Models and LiveData Binding
To prevent any UI-related data to be destroyed when android destroys and recreates the UI controller (eg. rotating your phone), we have to store data in [View Models](https://developer.android.com/topic/libraries/architecture/viewmodel) instead of in the Fragment/Activity.


```kotlin
private val viewModel: HomeTimelineViewModel by viewModels()
```

Then in the `onCreate` method, bind it to the layout:
```kotlin
binding.viewModel = viewModel
```

### Live Data
We also use [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - it is an observable data holder class that is also lifecycle aware. The observable live data:
```kotlin

viewModel.score.observe(viewLifecycleOwner, Observer { newScore ->
    binding.scoreText.text = newScore?.toString()
})
```


Tip
* Encapsulate live data by not exposing the `MutableLiveData` outside the viewModel. LiveData can read but cannot call set value on
```kotlin
private val _score = MutableLiveData<Int>()

val score : LiveData<Int>
        get() = _score
```
* Use a `ViewModelFactory` to initialize a ViewModel with certain parameters. When you request a viewmodel from the `ViewModelFactory` after the activity is recreated, you wll receive the same object as before. Don't have to worrya bout saving/restoring state.
* Bind your LiveData to the layout, so you don't have to observe them anymore!

Now that our View Model is bound to the layout, we can use data binding expressions in the layout files. These code in the activity file:
```kotlin
viewModel.score.observe(viewLifecycleOwner, Observer { newScore ->
    binding.scoreText.text = newScore?.toString()
})

binding.addOneButton.setOnClickListener {
    viewModel.addOne()
}
```
can be replaced by these lines in the layout file:
```xml
<data>
    <variable
        name="viewModel"
        type="com...HomeTimelineViewModel" />
</data>

<TextView
    android:id="@+id/scoreText"
    android:text="@{viewModel.score.toString()}"
    ... />

<Button
    android:id="@+id/addOneButton"
    android:onClick="@{() -> viewModel.addOne()}"
    ... />
```


# 5. Setting up API Service
Learn [Retrofit](https://square.github.io/retrofit/)

Tip:
* Create retrofit instance
* Create private service that uses retrofit instance then an API object to expose service
* Use Moshi to convert from JSON to Model
* Call the API in ViewModel
* Use Coroutines to replace enqueue method
```kotlin
WeiboApi.retrofitService.getComments().enqueue( object: Callback<List<Comment>>  {
    override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
        _response.value = "Failure: " + t.message
    }

    override fun onResponse(call: Call<List<Comment>>, response: Response<List<Comment>>) {
        _response.value = "Success: ${response.body()?.size} comments retrieved!"
    }

})
```
to a much shorter and cleaner version:
```kotlin
private var coroutineJob : Job? = null

private fun getComments() {
    coroutineJob = CoroutineScope(Dispatchers.IO).launch {
        var commentList = WeiboApi.retrofitService.getComments()
        withContext(Dispatchers.Main) {
            try {
                _response.value = "Success: ${commentList.size} comments retrieved!"
            } catch (t:Throwable) {
                _response.value = "Failure: " + t.message
            }
        }
    }
}
```
* Tip: To use repository to store/manage data. Especially if there is data from the database to be used as cache. 

# 6. Coroutines
* Used for API calls
* Used for Database transactions

Tip
* Can use LiveData
* Use Coroutine Scope
* Remember to cancel it in the `onCleared` method in ViewModel / whatever equivalent
```kotlin
coroutineJob = CoroutineScope(Dispatchers.IO).launch {
    var commentList = WeiboApi.retrofitService.getComments()
    withContext(Dispatchers.Main) {
        try {
            _comments.value = "Success: ${commentList.size} comments retrieved!"
        } catch (t:Throwable) {
            _comments.value = "Failure: " + t.message
        }
    }
}
```
Launch a IO scope to do computationally heavy/long tasks then change back to main thread. 

# 7. Recycler View

Tip
* Add plugin `kotlin-kapt` to use binding adapters
* Set up onClick listener for Recycler View. 

In RecyclerAdapter, set up a new listener class and allow the adapter to receive an instance of it as a class variable.
```kotlin
// Take in clickListener as part of the class variable
class PostGridAdapter(val clickListener: PostListener)

// Under ViewHolder - bind it
fun bind(post: TimelinePost, clickListener: PostListener) {
    binding.post = post
    binding.clickListener = clickListener
    binding.executePendingBindings()
}

override fun onBindViewHolder(holder: PostGridAdapter.TimelinePostViewHolder, position: Int) {
    val post = getItem(position)
    holder.bind(post, clickListener)
}

// Create a Listener class
class PostListener(val clickListener: (id: String) -> Unit) {
    fun onClick(post: TimelinePost) = clickListener(post.id)
}

```

In Fragment, when instantiating the adapter, write the onClick logic here
```kotlin
// In Fragment
binding.postsGrid.adapter = PostGridAdapter(PostListener {
    id -> Toast.makeText(context, "${id}", Toast.LENGTH_LONG).show()
})
```

In Layout xml file, add variable and bind to onclick.
```kotlin
// Add variable
<variable
    name="clickListener"
    type="com.thoughtworks.miniweibo.ui.home.PostListener" />

// Bind to layout / button
<androidx.constraintlayout.widget.ConstraintLayout
    ...
    android:onClick="@{() -> clickListener.onClick(post)}">
```