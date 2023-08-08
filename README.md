Easy Recorder By ChikuAI

Website : https://chikuaicode.com <br>
Instagram : https://instagram.com/chikuaicode  <br>
Facebook : https://facebook.com/chikuaifb <br>
YouTube : https://youtube.com/ChikuAi <br>


---------- Key Features

* Easily Record Audio
* Call Back Function
* Time Counter
* Proper Callback
* Support AAC format

========================== Usage ================

-- callback 

    void onStart();
    void onRecording(long dur);
    void onStop(String path);
    void onEvent(String message);

         

-- Initialize with Object and callback 

    EasyRecorder easyRecorder = new EasyRecorder(YourActivity.this, new EasyRecorder.ChikuAction() {
      @Override
       public void onStart() {
         // call on start recording
        }

       @Override
        public void onRecording(long dur) {
            //call every second while recoding with duration
        }

        @Override
         public void onStop(String path) {
             //call on stop with saved path
         }

         @Override
         public void onEvent(String message) {
              // Handle all event with message
         }
    });




-- start recoding

    audioRecord.setOnClickListener(v -> {
     if (easyRecorder.isRecording()) {
          // Already Recording
       } else {
           easyRecorder.start();
        }
     });


        
-- stop recording


    audioRecord.setOnClickListener(v -> {
      if (easyRecorder.isRecording()) {
            easyRecorder.stop();
      }else{
           // already stopped
       }
    });


-- set custom path if required

    easyRecorder.setPath(full_path,file_name_with_extension);
