# RxBus

RxBus, This is an event bus designed to allowing your application to communicate efficiently.

## Feature
- event bus by tag
- sticky event 
- generic support
- lifecycle support
- subscribe on other thread

## Usage
### post without value
```kotlin
// post
RxBus.getDefault<Any>().post("tag1")

// subscribe
RxBus.getDefault<Any>().receive(
    this,
    "tag1",
    object : Action1<Any> {
        override fun onReceive(data: Any) {
            Log.d(TAG, "receive1: tag=tag1, data=$data (${Thread.currentThread().name})")
        }
    }
)
// or
RxBus.getDefault<Any>().receive(
    this,
    "tag1"
) {
    Log.d(TAG, "receive2: tag=tag1, data=$it (${Thread.currentThread().name})")
}
```

### post with a value
```kotlin
// post
RxBus.getDefault<String>().post("tag2", "a test value")

// subscribe
RxBus.getDefault<String>().receive(
    this,
    "tag2"
) { data -> Log.d(TAG, "receive: tag=tag2, data=$data (${Thread.currentThread().name})") }

```

### subscribe on other thread
```kotlin
RxBus.getDefault<String>().receive(
    this, "tag2",
    { data ->
        Log.d(
            TAG,
            "receive: tag=tag2, data=$data (${Thread.currentThread().name})"
        )
    },
    Schedulers.io()
)
```

### sticky event
```kotlin
// post sticky event
RxBus.getDefault<Int>().postSticky("tag10", 10086)

// subscribe sticky
RxBus.getDefault<Int>().receiveSticky(this, "tag10") {
    Log.i(
        TAG,
        "receiveSticky: tag=tag10, data=$it (${Thread.currentThread().name})"
    )
}

// remove sticky event by tag
RxBus.getDefault<Int>().removeStickyEvent("tag10")
```

## License

    Copyright 2018 hacket

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.