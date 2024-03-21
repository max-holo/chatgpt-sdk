package com.unfbx.chatgpt;

import cn.hutool.core.util.StrUtil;
import com.unfbx.chatgpt.entity.billing.BillingUsage;
import com.unfbx.chatgpt.entity.billing.CreditGrantsResponse;
import com.unfbx.chatgpt.entity.billing.Subscription;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.ChatCompletionResponse;
import com.unfbx.chatgpt.entity.chat.Message;
import com.unfbx.chatgpt.entity.common.DeleteResponse;
import com.unfbx.chatgpt.entity.common.OpenAiResponse;
import com.unfbx.chatgpt.entity.completions.Completion;
import com.unfbx.chatgpt.entity.completions.CompletionResponse;
import com.unfbx.chatgpt.entity.edits.Edit;
import com.unfbx.chatgpt.entity.edits.EditResponse;
import com.unfbx.chatgpt.entity.embeddings.Embedding;
import com.unfbx.chatgpt.entity.embeddings.EmbeddingResponse;
import com.unfbx.chatgpt.entity.engines.Engine;
import com.unfbx.chatgpt.entity.files.UploadFileResponse;
import com.unfbx.chatgpt.entity.fineTune.Event;
import com.unfbx.chatgpt.entity.fineTune.FineTune;
import com.unfbx.chatgpt.entity.fineTune.FineTuneDeleteResponse;
import com.unfbx.chatgpt.entity.fineTune.FineTuneResponse;
import com.unfbx.chatgpt.entity.images.*;
import com.unfbx.chatgpt.entity.models.Model;
import com.unfbx.chatgpt.entity.models.ModelResponse;
import com.unfbx.chatgpt.entity.moderations.Moderation;
import com.unfbx.chatgpt.entity.moderations.ModerationResponse;
import com.unfbx.chatgpt.entity.whisper.Transcriptions;
import com.unfbx.chatgpt.entity.whisper.Translations;
import com.unfbx.chatgpt.entity.whisper.WhisperResponse;
import com.unfbx.chatgpt.exception.BaseException;
import com.unfbx.chatgpt.exception.CommonError;
import com.unfbx.chatgpt.function.KeyStrategy;
import com.unfbx.chatgpt.interceptor.DefaultOpenAiAuthInterceptor;
import com.unfbx.chatgpt.interceptor.OpenAiAuthInterceptor;
import io.reactivex.Single;
import okhttp3.MediaType;
import okhttp3.MultipartBody.Part;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.RequestBody;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class OpenAiClient {
    private static final Logger log = LoggerFactory.getLogger(OpenAiClient.class);
    protected String apiHost;
    protected OpenAiApi openAiApi;
    protected OkHttpClient okHttpClient;
    protected KeyStrategy keyStrategy;
    protected OpenAiAuthInterceptor authInterceptor;

    public OpenAiClient() {
    }

    protected OkHttpClient okHttpClient(KeyStrategy keyStrategy) {
        if (Objects.isNull(this.authInterceptor)) {
            this.authInterceptor = new DefaultOpenAiAuthInterceptor(keyStrategy);
        }

        return (new Builder()).addInterceptor(this.authInterceptor).connectTimeout(30L, TimeUnit.SECONDS).writeTimeout(30L, TimeUnit.SECONDS).readTimeout(30L, TimeUnit.SECONDS).build();
    }

    public List<Model> models() {
        Single<ModelResponse> models = this.openAiApi.models();
        List<Model> modelList = ((ModelResponse)models.blockingGet()).getData();
        return modelList;
    }

    public Model model(String id) {
        if (!Objects.isNull(id) && !"".equals(id)) {
            Single<Model> model = this.openAiApi.model(id);
            return (Model)model.blockingGet();
        } else {
            throw new BaseException(CommonError.PARAM_ERROR);
        }
    }

    public CompletionResponse completions(Completion completion) {
        Single<CompletionResponse> completions = this.openAiApi.completions(completion);
        return (CompletionResponse)completions.blockingGet();
    }

    public CompletionResponse completions(String question) {
        Completion q = Completion.builder().prompt(question).build();
        Single<CompletionResponse> completions = this.openAiApi.completions(q);
        return (CompletionResponse)completions.blockingGet();
    }

    public EditResponse edit(Edit edit) {
        Single<EditResponse> edits = this.openAiApi.edits(edit);
        return (EditResponse)edits.blockingGet();
    }

    public ImageResponse genImages(String prompt) {
        Image image = Image.builder().prompt(prompt).build();
        return this.genImages(image);
    }

    public ImageResponse genImages(Image image) {
        Single<ImageResponse> edits = this.openAiApi.genImages(image);
        return (ImageResponse)edits.blockingGet();
    }

    public List<Item> editImages(File image, String prompt) {
        ImageEdit imageEdit = ImageEdit.builder().prompt(prompt).build();
        return this.editImages(image, (File)null, imageEdit);
    }

    public List<Item> editImages(File image, ImageEdit imageEdit) {
        return this.editImages(image, (File)null, imageEdit);
    }

    public List<Item> editImages(File image, File mask, ImageEdit imageEdit) {
        this.checkImage(image);
        this.checkImageFormat(image);
        this.checkImageSize(image);
        if (Objects.nonNull(mask)) {
            this.checkImageFormat(image);
            this.checkImageSize(image);
        }

        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), image);
        Part imageMultipartBody = Part.createFormData("image", image.getName(), imageBody);
        Part maskMultipartBody = null;
        if (Objects.nonNull(mask)) {
            RequestBody maskBody = RequestBody.create(MediaType.parse("multipart/form-data"), mask);
            maskMultipartBody = Part.createFormData("mask", image.getName(), maskBody);
        }

        Map<String, RequestBody> requestBodyMap = new HashMap();
        requestBodyMap.put("prompt", RequestBody.create(MediaType.parse("multipart/form-data"), imageEdit.getPrompt()));
        requestBodyMap.put("n", RequestBody.create(MediaType.parse("multipart/form-data"), imageEdit.getN().toString()));
        requestBodyMap.put("size", RequestBody.create(MediaType.parse("multipart/form-data"), imageEdit.getSize()));
        requestBodyMap.put("response_format", RequestBody.create(MediaType.parse("multipart/form-data"), imageEdit.getResponseFormat()));
        if (!Objects.isNull(imageEdit.getUser()) && !"".equals(imageEdit.getUser())) {
            requestBodyMap.put("user", RequestBody.create(MediaType.parse("multipart/form-data"), imageEdit.getUser()));
        }

        Single<ImageResponse> imageResponse = this.openAiApi.editImages(imageMultipartBody, maskMultipartBody, requestBodyMap);
        return ((ImageResponse)imageResponse.blockingGet()).getData();
    }

    public ImageResponse variationsImages(File image, ImageVariations imageVariations) {
        this.checkImage(image);
        this.checkImageFormat(image);
        this.checkImageSize(image);
        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), image);
        Part multipartBody = Part.createFormData("image", image.getName(), imageBody);
        Map<String, RequestBody> requestBodyMap = new HashMap();
        requestBodyMap.put("n", RequestBody.create(MediaType.parse("multipart/form-data"), imageVariations.getN().toString()));
        requestBodyMap.put("size", RequestBody.create(MediaType.parse("multipart/form-data"), imageVariations.getSize()));
        requestBodyMap.put("response_format", RequestBody.create(MediaType.parse("multipart/form-data"), imageVariations.getResponseFormat()));
        if (!Objects.isNull(imageVariations.getUser()) && !"".equals(imageVariations.getUser())) {
            requestBodyMap.put("user", RequestBody.create(MediaType.parse("multipart/form-data"), imageVariations.getUser()));
        }

        Single<ImageResponse> variationsImages = this.openAiApi.variationsImages(multipartBody, requestBodyMap);
        return (ImageResponse)variationsImages.blockingGet();
    }

    public ImageResponse variationsImages(File image) {
        this.checkImage(image);
        this.checkImageFormat(image);
        this.checkImageSize(image);
        ImageVariations imageVariations = ImageVariations.builder().build();
        return this.variationsImages(image, imageVariations);
    }

    private void checkImage(File image) {
        if (Objects.isNull(image)) {
            log.error("image不能为空");
            throw new BaseException(CommonError.PARAM_ERROR);
        }
    }

    private void checkImageFormat(File image) {
        if (!image.getName().endsWith("png") && !image.getName().endsWith("PNG")) {
            log.error("image格式错误");
            throw new BaseException(CommonError.PARAM_ERROR);
        }
    }

    private void checkImageSize(File image) {
        if (image.length() > 4194304L) {
            log.error("image最大支持4MB");
            throw new BaseException(CommonError.PARAM_ERROR);
        }
    }

    public EmbeddingResponse embeddings(String input) {
        List<String> inputs = new ArrayList(1);
        inputs.add(input);
        Embedding embedding = Embedding.builder().input(inputs).build();
        return this.embeddings(embedding);
    }

    public EmbeddingResponse embeddings(List<String> input) {
        Embedding embedding = Embedding.builder().input(input).build();
        return this.embeddings(embedding);
    }

    public EmbeddingResponse embeddings(Embedding embedding) {
        Single<EmbeddingResponse> embeddings = this.openAiApi.embeddings(embedding);
        return (EmbeddingResponse)embeddings.blockingGet();
    }

    public List<com.unfbx.chatgpt.entity.files.File> files() {
        Single<OpenAiResponse<com.unfbx.chatgpt.entity.files.File>> files = this.openAiApi.files();
        return ((OpenAiResponse)files.blockingGet()).getData();
    }

    public DeleteResponse deleteFile(String fileId) {
        Single<DeleteResponse> deleteFile = this.openAiApi.deleteFile(fileId);
        return (DeleteResponse)deleteFile.blockingGet();
    }

    public UploadFileResponse uploadFile(String purpose, File file) {
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        Part multipartBody = Part.createFormData("file", file.getName(), fileBody);
        RequestBody purposeBody = RequestBody.create(MediaType.parse("multipart/form-data"), purpose);
        Single<UploadFileResponse> uploadFileResponse = this.openAiApi.uploadFile(multipartBody, purposeBody);
        return (UploadFileResponse)uploadFileResponse.blockingGet();
    }

    public UploadFileResponse uploadFile(File file) {
        return this.uploadFile("fine-tune", file);
    }

    public com.unfbx.chatgpt.entity.files.File retrieveFile(String fileId) {
        Single<com.unfbx.chatgpt.entity.files.File> fileContent = this.openAiApi.retrieveFile(fileId);
        return (com.unfbx.chatgpt.entity.files.File)fileContent.blockingGet();
    }

    public ModerationResponse moderations(String input) {
        List<String> content = new ArrayList(1);
        content.add(input);
        Moderation moderation = Moderation.builder().input(content).build();
        return this.moderations(moderation);
    }

    public ModerationResponse moderations(List<String> input) {
        Moderation moderation = Moderation.builder().input(input).build();
        return this.moderations(moderation);
    }

    public ModerationResponse moderations(Moderation moderation) {
        Single<ModerationResponse> moderations = this.openAiApi.moderations(moderation);
        return (ModerationResponse)moderations.blockingGet();
    }

    public FineTuneResponse fineTune(FineTune fineTune) {
        Single<FineTuneResponse> fineTuneResponse = this.openAiApi.fineTune(fineTune);
        return (FineTuneResponse)fineTuneResponse.blockingGet();
    }

    public FineTuneResponse fineTune(String trainingFileId) {
        FineTune fineTune = FineTune.builder().trainingFile(trainingFileId).build();
        return this.fineTune(fineTune);
    }

    public List<FineTuneResponse> fineTunes() {
        Single<OpenAiResponse<FineTuneResponse>> fineTunes = this.openAiApi.fineTunes();
        return ((OpenAiResponse)fineTunes.blockingGet()).getData();
    }

    public FineTuneResponse retrieveFineTune(String fineTuneId) {
        Single<FineTuneResponse> fineTune = this.openAiApi.retrieveFineTune(fineTuneId);
        return (FineTuneResponse)fineTune.blockingGet();
    }

    public FineTuneResponse cancelFineTune(String fineTuneId) {
        Single<FineTuneResponse> fineTune = this.openAiApi.cancelFineTune(fineTuneId);
        return (FineTuneResponse)fineTune.blockingGet();
    }

    public List<Event> fineTuneEvents(String fineTuneId) {
        Single<OpenAiResponse<Event>> events = this.openAiApi.fineTuneEvents(fineTuneId);
        return ((OpenAiResponse)events.blockingGet()).getData();
    }

    public FineTuneDeleteResponse deleteFineTuneModel(String model) {
        Single<FineTuneDeleteResponse> delete = this.openAiApi.deleteFineTuneModel(model);
        return (FineTuneDeleteResponse)delete.blockingGet();
    }

    /** @deprecated */
    @Deprecated
    public List<Engine> engines() {
        Single<OpenAiResponse<Engine>> engines = this.openAiApi.engines();
        return ((OpenAiResponse)engines.blockingGet()).getData();
    }

    /** @deprecated */
    @Deprecated
    public Engine engine(String engineId) {
        Single<Engine> engine = this.openAiApi.engine(engineId);
        return (Engine)engine.blockingGet();
    }

    public ChatCompletionResponse chatCompletion(ChatCompletion chatCompletion) {
        Single<ChatCompletionResponse> chatCompletionResponse = this.openAiApi.chatCompletion(chatCompletion);
        return (ChatCompletionResponse)chatCompletionResponse.blockingGet();
    }

    public ChatCompletionResponse chatCompletion(List<Message> messages) {
        ChatCompletion chatCompletion = ChatCompletion.builder().messages(messages).build();
        return this.chatCompletion(chatCompletion);
    }

    public WhisperResponse speechToTextTranscriptions(File file, Transcriptions transcriptions) {
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        Part multipartBody = Part.createFormData("file", file.getName(), fileBody);
        Map<String, RequestBody> requestBodyMap = new HashMap();
        if (StrUtil.isNotBlank(transcriptions.getLanguage())) {
            requestBodyMap.put("language", RequestBody.create(MediaType.parse("multipart/form-data"), transcriptions.getLanguage()));
        }

        if (StrUtil.isNotBlank(transcriptions.getModel())) {
            requestBodyMap.put("model", RequestBody.create(MediaType.parse("multipart/form-data"), transcriptions.getModel()));
        }

        if (StrUtil.isNotBlank(transcriptions.getPrompt())) {
            requestBodyMap.put("prompt", RequestBody.create(MediaType.parse("multipart/form-data"), transcriptions.getPrompt()));
        }

        if (StrUtil.isNotBlank(transcriptions.getResponseFormat())) {
            requestBodyMap.put("responseFormat", RequestBody.create(MediaType.parse("multipart/form-data"), transcriptions.getResponseFormat()));
        }

        if (Objects.nonNull(transcriptions.getTemperature())) {
            requestBodyMap.put("temperature", RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(transcriptions.getTemperature())));
        }

        Single<WhisperResponse> whisperResponse = this.openAiApi.speechToTextTranscriptions(multipartBody, requestBodyMap);
        return (WhisperResponse)whisperResponse.blockingGet();
    }

    public WhisperResponse speechToTextTranscriptions(File file) {
        Transcriptions transcriptions = Transcriptions.builder().build();
        return this.speechToTextTranscriptions(file, transcriptions);
    }

    public WhisperResponse speechToTextTranslations(File file, Translations translations) {
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        Part multipartBody = Part.createFormData("file", file.getName(), fileBody);
        Map<String, RequestBody> requestBodyMap = new HashMap();
        if (StrUtil.isNotBlank(translations.getModel())) {
            requestBodyMap.put("model", RequestBody.create(MediaType.parse("multipart/form-data"), translations.getModel()));
        }

        if (StrUtil.isNotBlank(translations.getPrompt())) {
            requestBodyMap.put("prompt", RequestBody.create(MediaType.parse("multipart/form-data"), translations.getPrompt()));
        }

        if (StrUtil.isNotBlank(translations.getResponseFormat())) {
            requestBodyMap.put("responseFormat", RequestBody.create(MediaType.parse("multipart/form-data"), translations.getResponseFormat()));
        }

        if (Objects.nonNull(translations.getTemperature())) {
            requestBodyMap.put("temperature", RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(translations.getTemperature())));
        }

        Single<WhisperResponse> whisperResponse = this.openAiApi.speechToTextTranslations(multipartBody, requestBodyMap);
        return (WhisperResponse)whisperResponse.blockingGet();
    }

    public WhisperResponse speechToTextTranslations(File file) {
        Translations translations = Translations.builder().build();
        return this.speechToTextTranslations(file, translations);
    }

    private void checkSpeechFileSize(File file) {
        if (file.length() > 30822400L) {
            log.warn("2023-03-02官方文档提示：文件不能超出25MB");
        }

    }

    /** @deprecated */
    @Deprecated
    public CreditGrantsResponse creditGrants() {
        Single<CreditGrantsResponse> creditGrants = this.openAiApi.creditGrants();
        return (CreditGrantsResponse)creditGrants.blockingGet();
    }

    public Subscription subscription() {
        Single<Subscription> subscription = this.openAiApi.subscription();
        return (Subscription)subscription.blockingGet();
    }

    public BillingUsage billingUsage(@NotNull LocalDate starDate, @NotNull LocalDate endDate) {
        Single<BillingUsage> billingUsage = this.openAiApi.billingUsage(starDate, endDate);
        return (BillingUsage)billingUsage.blockingGet();
    }

    public String getApiHost() {
        return this.apiHost;
    }

    public OpenAiApi getOpenAiApi() {
        return this.openAiApi;
    }

    public OkHttpClient getOkHttpClient() {
        return this.okHttpClient;
    }

    public KeyStrategy getKeyStrategy() {
        return this.keyStrategy;
    }

    public OpenAiAuthInterceptor getAuthInterceptor() {
        return this.authInterceptor;
    }
}
