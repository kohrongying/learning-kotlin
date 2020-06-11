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

# 3. Setting up API Service
Learn [Retrofit](https://square.github.io/retrofit/)

Tip:
* Create retrofit instance
* Create private service that uses retrofit instance then an API object to expose service
* Call the API in ViewModel
* Use Coroutines when using API 


# 4. Data binding
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


# 5. View Models and LiveData Binding
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


# 6. Coroutines
* Used for API calls
* Used for Database transactions