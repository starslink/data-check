package com.enjplatform.reconciliation.config;

import com.enjplatform.reconciliation.after.AfterCheckProcessor;
import com.enjplatform.reconciliation.after.CheckAfter;
import com.enjplatform.reconciliation.after.CheckSync;
import com.enjplatform.reconciliation.before.BeforeCheckProcessor;
import com.enjplatform.reconciliation.before.CheckPre;
import com.enjplatform.reconciliation.before.ResourceLoader;
import com.enjplatform.reconciliation.before.ResourceReader;
import com.enjplatform.reconciliation.check.CheckProcessor;
import com.enjplatform.reconciliation.enums.CheckCacheEnum;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

/**
 * ��������
 *
 * @author starslink
 */
@Data
@Builder
public class CheckConfig {

    /**
     * ����Ψһ��ʶ:���ȹ�����idΪ�� name��id����ͬʱΪ��
     */
    private String id;

    /**
     * ��������: ���ƿ����ظ�
     */
    private String name;

    /**
     * �����ζ�������(��ֹ�ڴ�й©) �Ǳ���
     */
    private Integer batchSize;

    /**
     * �Ƿ���̺߳˶�����(Ĭ�ϵ��߳�) �Ǳ���
     */
    private Boolean isAsync;

    /**
     * ��������Χ �Աȷ�ΧMath.abs(allowableErrorRange)
     */
    private BigDecimal allowableErrorRange;

    /**
     * ʹ�ú��ֶ��˷�ʽ(Ĭ���ڴ�) �Ǳ���
     */
    private CheckCacheEnum checkCacheEnum;

    /**
     * redis����
     */
    private RedisConfig redisConfig;

    //ǰ�ô���������-----------------------------------

    /**
     * �Զ���ǰ�ô�����:�ɲ�������ResourceReader
     */
    private BeforeCheckProcessor beforeCheckProcessor;

    /**
     * ǰ��У��: ��Ϊ��
     */
    private CheckPre checkPre;

    /**
     * ��������Դ
     */
    private ResourceLoader srcLoader;

    /**
     * ��������Դ
     */
    private ResourceLoader targetLoader;

    /**
     * ����Դ��ȡ (����������)
     */
    private ResourceReader resourceReader;

    //���˴���������-----------------------------------

    /**
     * �Զ�����˴�����
     */
    private CheckProcessor checkProcessor;

    //���ô���������-----------------------------------

    /**
     * �Զ�����ô�����: �ɲ�������CheckSync
     */
    private AfterCheckProcessor afterCheckProcessor;

    /**
     * ���˽��ͬ��������: ��Ϊ��
     */
    private CheckSync checkSync;

    /**
     * ���˺��ô����߼�: ��Ϊ��
     */
    private CheckAfter checkAfter;
}
