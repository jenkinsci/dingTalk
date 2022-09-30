import{_ as s,c as n,o as a,a as l}from"./app.77e7e65f.js";const p="/dingtalk-plugin/assets/dtmt-example.465e07b8.jpg",F=JSON.parse('{"title":"DTMD \u534F\u8BAE\u7684\u6D88\u606F","description":"","frontmatter":{},"headers":[],"relativePath":"examples/dtmd.md"}'),o={name:"examples/dtmd.md"},e=l(`<h1 id="dtmd-\u534F\u8BAE\u7684\u6D88\u606F" tabindex="-1">DTMD \u534F\u8BAE\u7684\u6D88\u606F <a class="header-anchor" href="#dtmd-\u534F\u8BAE\u7684\u6D88\u606F" aria-hidden="true">#</a></h1><p><a href="https://m.dingtalk.com/qidian/help-detail-1060976699" target="_blank" rel="noreferrer">\u652F\u6301\u7B80\u5355\u7684\u5355\u5411\u4EA4\u4E92</a></p><div class="warning custom-block"><p class="custom-block-title">WARNING</p><p><code>dtmd</code> \u534F\u8BAE\u53EA\u80FD\u5728markdown\u3001actioncard\u3001feedcard \u6D88\u606F\u7C7B\u578B\u4E2D\u4F7F\u7528</p></div><div class="language-groovy"><button class="copy"></button><span class="lang">groovy</span><pre><code><span class="line"></span>
<span class="line"><span style="color:#A6ACCD;">pipeline {</span></span>
<span class="line"><span style="color:#A6ACCD;">    agent any</span></span>
<span class="line"><span style="color:#A6ACCD;">    stages {</span></span>
<span class="line"><span style="color:#A6ACCD;">        stage</span><span style="color:#89DDFF;">(</span><span style="color:#89DDFF;">&#39;</span><span style="color:#C3E88D;">link</span><span style="color:#89DDFF;">&#39;</span><span style="color:#89DDFF;">)</span><span style="color:#A6ACCD;">{</span></span>
<span class="line"><span style="color:#A6ACCD;">            steps {</span></span>
<span class="line"><span style="color:#A6ACCD;">                echo </span><span style="color:#89DDFF;">&#39;</span><span style="color:#C3E88D;">\u6D4B\u8BD5 MARKDOWN \u6D88\u606F...</span><span style="color:#89DDFF;">&#39;</span></span>
<span class="line"><span style="color:#A6ACCD;">            }</span></span>
<span class="line"><span style="color:#A6ACCD;">            post {</span></span>
<span class="line"><span style="color:#A6ACCD;">                success {</span></span>
<span class="line"><span style="color:#A6ACCD;">                    dingtalk (</span></span>
<span class="line"><span style="color:#A6ACCD;">                        </span><span style="color:#FFCB6B;">robot</span><span style="color:#89DDFF;">:</span><span style="color:#A6ACCD;"> </span><span style="color:#89DDFF;">&#39;</span><span style="color:#C3E88D;">3141dbb8-9d32-4344-8324-df6e2b522117</span><span style="color:#89DDFF;">&#39;</span><span style="color:#A6ACCD;">,</span></span>
<span class="line"><span style="color:#A6ACCD;">                        </span><span style="color:#FFCB6B;">type</span><span style="color:#89DDFF;">:</span><span style="color:#A6ACCD;"> </span><span style="color:#89DDFF;">&#39;</span><span style="color:#C3E88D;">MARKDOWN</span><span style="color:#89DDFF;">&#39;</span><span style="color:#A6ACCD;">,</span></span>
<span class="line"><span style="color:#A6ACCD;">                        </span><span style="color:#FFCB6B;">title</span><span style="color:#89DDFF;">:</span><span style="color:#A6ACCD;"> </span><span style="color:#89DDFF;">&#39;</span><span style="color:#C3E88D;">\u4F60\u6709\u65B0\u7684\u6D88\u606F\uFF0C\u8BF7\u6CE8\u610F\u67E5\u6536</span><span style="color:#89DDFF;">&#39;</span><span style="color:#A6ACCD;">,</span></span>
<span class="line"><span style="color:#A6ACCD;">                        </span><span style="color:#FFCB6B;">text</span><span style="color:#89DDFF;">:</span><span style="color:#A6ACCD;"> </span><span style="color:#89DDFF;">[</span></span>
<span class="line"><span style="color:#A6ACCD;">                            </span><span style="color:#89DDFF;">&#39;</span><span style="color:#C3E88D;"># DTMD \u6D88\u606F</span><span style="color:#89DDFF;">&#39;</span><span style="color:#89DDFF;">,</span></span>
<span class="line"><span style="color:#A6ACCD;">                            </span><span style="color:#89DDFF;">&#39;</span><span style="color:#C3E88D;">- [\u70B9\u6211](dtmd://dingtalkclient/sendMessage?content=\u4F60\u597D)</span><span style="color:#89DDFF;">&#39;</span><span style="color:#89DDFF;">,</span></span>
<span class="line"><span style="color:#A6ACCD;">                            </span><span style="color:#89DDFF;">&#39;</span><span style="color:#C3E88D;">- [\u518D\u70B9](dtmd://dingtalkclient/sendMessage?content=\u50BB\u903C)</span><span style="color:#89DDFF;">&#39;</span></span>
<span class="line"><span style="color:#A6ACCD;">                        </span><span style="color:#89DDFF;">]</span></span>
<span class="line"><span style="color:#A6ACCD;">                    )</span></span>
<span class="line"><span style="color:#A6ACCD;">                }</span></span>
<span class="line"><span style="color:#A6ACCD;">            }</span></span>
<span class="line"><span style="color:#A6ACCD;">        }</span></span>
<span class="line"><span style="color:#A6ACCD;">    }</span></span>
<span class="line"><span style="color:#A6ACCD;">}</span></span>
<span class="line"></span>
<span class="line"></span></code></pre></div><details class="details custom-block"><summary>\u67E5\u770B\u7ED3\u679C</summary><p><img src="`+p+'" alt="dtmt-example"></p></details>',5),t=[e];function c(r,D,C,y,i,A){return a(),n("div",null,t)}const m=s(o,[["render",c]]);export{F as __pageData,m as default};
