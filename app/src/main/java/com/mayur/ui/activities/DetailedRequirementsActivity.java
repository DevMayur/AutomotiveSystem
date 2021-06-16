package com.mayur.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.mayur.R;

import io.github.kbiakov.codeview.CodeView;
import io.github.kbiakov.codeview.adapters.Options;
import io.github.kbiakov.codeview.highlight.ColorTheme;

public class DetailedRequirementsActivity extends AppCompatActivity {

    CodeView gitclone,python_packages,import_protos,test_installation,success_message,xml_to_csv1,xml_to_csv2,pbtxt_py,generating_tfrecords,creating_labelmap,setting_up_configurations,training_model,export_frozen_graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_requirements);

        initCodeViews();
        setCodeViews();

    }

    private void setCodeViews() {

        setCodeText(gitclone,"git clone https://github.com/tensorflow/models.git");
        setCodeText(python_packages,"cd models/research\n" +
                "# Compile protos.\n" +
                "protoc object_detection/protos/*.proto --python_out=.\n" +
                "# Install TensorFlow Object Detection API.\n" +
                "cp object_detection/packages/tf1/setup.py .\n" +
                "python -m pip install .");
        setCodeText(import_protos,"import os\n" +
                "import sys\n" +
                "args = sys.argv\n" +
                "directory = args[1]\n" +
                "protoc_path = args[2]\n" +
                "for file in os.listdir(directory):\n" +
                "    if file.endswith(\".proto\"):\n" +
                "        os.system(protoc_path+\" \"+directory+\"/\"+file+\" --python_out=.\")" +
                "\n\n python use_protobuf.py <path to directory> <path to protoc file>");
        setCodeText(test_installation,"# Test the installation.\n" +
                "python object_detection/builders/model_builder_tf1_test.py");
        setCodeText(success_message,"Running tests under Python 3.6.9: /usr/bin/python3\n" +
                "[ RUN      ] ModelBuilderTF1Test.test_create_context_rcnn_from_config_with_params(True)\n" +
                "[       OK ] ModelBuilderTF1Test.test_create_context_rcnn_from_config_with_params(True)\n" +
                "[ RUN      ] ModelBuilderTF1Test.test_create_context_rcnn_from_config_with_params(False)\n" +
                "[       OK ] ModelBuilderTF1Test.test_create_context_rcnn_from_config_with_params(False)\n" +
                "[ RUN      ] ModelBuilderTF1Test.test_create_experimental_model\n" +
                "[       OK ] ModelBuilderTF1Test.test_create_experimental_model\n" +
                "[ RUN      ] ModelBuilderTF1Test.test_create_faster_rcnn_from_config_with_crop_feature(True)\n" +
                "[       OK ] ModelBuilderTF1Test.test_create_faster_rcnn_from_config_with_crop_feature(True)\n" +
                "[ RUN      ] ModelBuilderTF1Test.test_create_faster_rcnn_from_config_with_crop_feature(False)\n" +
                "[       OK ] ModelBuilderTF1Test.test_create_faster_rcnn_from_config_with_crop_feature(False)\n" +
                "[ RUN      ] ModelBuilderTF1Test.test_create_faster_rcnn_model_from_config_with_example_miner\n" +
                "[       OK ] ModelBuilderTF1Test.test_create_faster_rcnn_model_from_config_with_example_miner\n" +
                "[ RUN      ] ModelBuilderTF1Test.test_create_faster_rcnn_models_from_config_faster_rcnn_with_matmul\n" +
                "[       OK ] ModelBuilderTF1Test.test_create_faster_rcnn_models_from_config_faster_rcnn_with_matmul\n" +
                "[ RUN      ] ModelBuilderTF1Test.test_create_faster_rcnn_models_from_config_faster_rcnn_without_matmul\n" +
                "[       OK ] ModelBuilderTF1Test.test_create_faster_rcnn_models_from_config_faster_rcnn_without_matmul\n" +
                "[ RUN      ] ModelBuilderTF1Test.test_create_faster_rcnn_models_from_config_mask_rcnn_with_matmul\n" +
                "[       OK ] ModelBuilderTF1Test.test_create_faster_rcnn_models_from_config_mask_rcnn_with_matmul\n" +
                "[ RUN      ] ModelBuilderTF1Test.test_create_faster_rcnn_models_from_config_mask_rcnn_without_matmul\n" +
                "[       OK ] ModelBuilderTF1Test.test_create_faster_rcnn_models_from_config_mask_rcnn_without_matmul\n" +
                "[ RUN      ] ModelBuilderTF1Test.test_create_rfcn_model_from_config\n" +
                "[       OK ] ModelBuilderTF1Test.test_create_rfcn_model_from_config\n" +
                "[ RUN      ] ModelBuilderTF1Test.test_create_ssd_fpn_model_from_config\n" +
                "[       OK ] ModelBuilderTF1Test.test_create_ssd_fpn_model_from_config\n" +
                "[ RUN      ] ModelBuilderTF1Test.test_create_ssd_models_from_config\n" +
                "[       OK ] ModelBuilderTF1Test.test_create_ssd_models_from_config\n" +
                "[ RUN      ] ModelBuilderTF1Test.test_invalid_faster_rcnn_batchnorm_update\n" +
                "[       OK ] ModelBuilderTF1Test.test_invalid_faster_rcnn_batchnorm_update\n" +
                "[ RUN      ] ModelBuilderTF1Test.test_invalid_first_stage_nms_iou_threshold\n" +
                "[       OK ] ModelBuilderTF1Test.test_invalid_first_stage_nms_iou_threshold\n" +
                "[ RUN      ] ModelBuilderTF1Test.test_invalid_model_config_proto\n" +
                "[       OK ] ModelBuilderTF1Test.test_invalid_model_config_proto\n" +
                "[ RUN      ] ModelBuilderTF1Test.test_invalid_second_stage_batch_size\n" +
                "[       OK ] ModelBuilderTF1Test.test_invalid_second_stage_batch_size\n" +
                "[ RUN      ] ModelBuilderTF1Test.test_session\n" +
                "[  SKIPPED ] ModelBuilderTF1Test.test_session\n" +
                "[ RUN      ] ModelBuilderTF1Test.test_unknown_faster_rcnn_feature_extractor\n" +
                "[       OK ] ModelBuilderTF1Test.test_unknown_faster_rcnn_feature_extractor\n" +
                "[ RUN      ] ModelBuilderTF1Test.test_unknown_meta_architecture\n" +
                "[       OK ] ModelBuilderTF1Test.test_unknown_meta_architecture\n" +
                "[ RUN      ] ModelBuilderTF1Test.test_unknown_ssd_feature_extractor\n" +
                "[       OK ] ModelBuilderTF1Test.test_unknown_ssd_feature_extractor\n" +
                "----------------------------------------------------------------------\n" +
                "Ran 21 tests in 0.163s\n" +
                "\n" +
                "OK (skipped=1)\n");
        setCodeText(xml_to_csv1,"def main():\n" +
                "    for folder in ['train', 'test']:\n" +
                "        image_path = os.path.join(os.getcwd(), ('images/' + folder))\n" +
                "        xml_df = xml_to_csv(image_path)\n" +
                "        xml_df.to_csv(('images/'+folder+'_labels.csv'), index=None)\n" +
                "\tprint('Successfully converted xml to csv.')");
        setCodeText(xml_to_csv2,"python xml_to_csv.py");
        setCodeText(pbtxt_py,"def class_text_to_int(row_label):\n" +
                "    if row_label == 'main road':\n" +
                "        return 1\n" +
                "    elif row_label == 'give road':\n" +
                "        return 2\n" +
                "    elif row_label == 'dont overtake':\n" +
                "        return 3\n" +
                "    elif row_label == 'no parking':\n" +
                "        return 4\n" +
                "    else:\n" +
                "        None");
        setCodeText(generating_tfrecords,"python generate_tfrecord.py --csv_input=images/train_labels.csv --image_dir=images/train --output_path=train.record\n" +
                "python generate_tfrecord.py --csv_input=images/test_labels.csv --image_dir=images/test --output_path=test.record");
        setCodeText(creating_labelmap,"\n" +
                "item {\n" +
                "    id: 1\n" +
                "    name: 'main road'\n" +
                "}\n" +
                "item {\n" +
                "    id: 2\n" +
                "    name: 'give road'\n" +
                "}\n" +
                "item {\n" +
                "    id: 3\n" +
                "    name: 'dont overtake'\n" +
                "}\n" +
                "item {\n" +
                "    id: 4\n" +
                "    name: 'no parking'\n}");
        setCodeText(setting_up_configurations,"train_input_reader {\n" +
                "  label_map_path: \"PATH_TO_BE_CONFIGURED/mscoco_label_map.pbtxt\"\n" +
                "  tf_record_input_reader {\n" +
                "    input_path: \"PATH_TO_BE_CONFIGURED/mscoco_train.record-00000-of-00100\"\n" +
                "  }\n" +
                "}\n" +
                "eval_config {\n" +
                "  num_examples: 8000\n" +
                "  metrics_set: \"coco_detection_metrics\"\n" +
                "  use_moving_averages: true\n" +
                "  include_metrics_per_category: true\n" +
                "}\n" +
                "eval_input_reader {\n" +
                "  label_map_path: \"PATH_TO_BE_CONFIGURED/mscoco_label_map.pbtxt\"\n" +
                "  shuffle: false\n" +
                "  num_readers: 1\n" +
                "  tf_record_input_reader {\n" +
                "    input_path: \"PATH_TO_BE_CONFIGURED/mscoco_val.record-00000-of-00010\"");
        setCodeText(training_model,"python model_main.py --logtostderr --model_dir=training/ --pipeline_config_path=training/ssd_mobilenet_v2_quantized_300x300_coco.config");
        setCodeText(export_frozen_graph,"python export_tflite_ssd_graph.py");


    }

    private void setCodeText(CodeView codeView, String text) {

        codeView.setClickable(false);
        codeView.setNestedScrollingEnabled(true);
        codeView.setOptions(Options.Default.get(this)
                .withLanguage("py")
                .withCode(text)
                .withTheme(ColorTheme.MONOKAI));
    }

    private void initCodeViews() {
        gitclone = findViewById(R.id.git_clone);
        python_packages = findViewById(R.id.python_package);
        import_protos = findViewById(R.id.proto_files);
        test_installation = findViewById(R.id.test_installation);
        success_message = findViewById(R.id.success_message);
        xml_to_csv1 = findViewById(R.id.xml_to_csv1);
        xml_to_csv2 = findViewById(R.id.xml_to_csv2);
        pbtxt_py = findViewById(R.id.pbtxt_py);
        generating_tfrecords = findViewById(R.id.generating_tfrecords);
        creating_labelmap = findViewById(R.id.creating_labelmap);
        setting_up_configurations = findViewById(R.id.setting_up_configurations);
        training_model = findViewById(R.id.training_model);
        export_frozen_graph = findViewById(R.id.export_frozen_graph);
    }
}